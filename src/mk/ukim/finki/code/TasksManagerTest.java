package mk.ukim.finki.code;

import com.sun.security.auth.UnixNumericUserPrincipal;

import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

interface Task{
    LocalDateTime getDeadline();

    Integer getPriority();

    String toString();

    default long getTimeLeft(){
        return Math.abs (Duration.between (LocalDateTime.now (),getDeadline ()).getSeconds ());
    }
}
class SimpleTask implements Task{

    String name;
    String description;

    public SimpleTask (String name, String description) {
        this.name = name;
        this.description = description;
    }

    @Override
    public LocalDateTime getDeadline () {
        return LocalDateTime.MAX;
    }

    @Override
    public Integer getPriority () {
        return Integer.MAX_VALUE;
    }

    @Override
    public String toString () {
        return "Task{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
abstract class TaskDecorator implements Task{
    Task wrappedTask;

    public TaskDecorator (Task wrappedTask) {
        this.wrappedTask = wrappedTask;
    }

    @Override
    public LocalDateTime getDeadline () {
        return wrappedTask.getDeadline ();
    }

    @Override
    public Integer getPriority () {
        return wrappedTask.getPriority ();
    }
}
class DeadlineDecorator extends TaskDecorator{

    LocalDateTime deadline;

    public DeadlineDecorator (Task wrappedTask,LocalDateTime deadline) {
        super (wrappedTask);
        this.deadline = deadline;
    }

    @Override
    public LocalDateTime getDeadline () {
        return deadline;
    }

    @Override
    public Integer getPriority () {
        return wrappedTask.getPriority ();
    }

    @Override
    public String toString () {
        return wrappedTask.toString () + ", deadline=" + deadline;
    }
}
class PriorityDecorator extends TaskDecorator{
    Integer priority;

    public PriorityDecorator (Task wrappedTask, Integer priority) {
        super (wrappedTask);
        this.priority = priority;
    }

    @Override
    public LocalDateTime getDeadline () {
        return wrappedTask.getDeadline ();
    }

    @Override
    public Integer getPriority () {
        return priority;
    }

    @Override
    public String toString () {
        return wrappedTask.toString () + ", priority=" + priority;
    }
}
class TaskCreator{
    //[категорија][име_на_задача],[oпис],[рок_за_задачата],[приоритет]
    //School,NP,lab 1 po NP,2020-06-23T23:59:59.000,1

    public static Task createTask(String line) throws DeadlineNotValidException {
        String[] parts = line.split (",");
        String name = parts[1];
        String desc = parts[2];
        Task simpleTask = new SimpleTask (name,desc);

        if (parts.length==3)
            return simpleTask;
        if (parts.length==4){
            try {
                Integer priority = Integer.parseInt (parts[3]);
                return new PriorityDecorator (simpleTask,priority);
            }catch (Exception e){
                LocalDateTime deadline = LocalDateTime.parse (parts[3]);
                checkDeadline (deadline);
                return new DeadlineDecorator (simpleTask,deadline);
            }
        }else {
            LocalDateTime deadline = LocalDateTime.parse (parts[3]);
            checkDeadline (deadline);
            Integer priority = Integer.parseInt (parts[4]);
            return new PriorityDecorator (new DeadlineDecorator (simpleTask,deadline),priority);
        }
    }
    private static void checkDeadline(LocalDateTime deadline) throws DeadlineNotValidException {
        if (deadline.isBefore (LocalDateTime.now ()))
            throw new DeadlineNotValidException(deadline);
    }
    public static String getCategory(String line){
        return line.split (",")[0];
    }
}
class DeadlineNotValidException extends Exception{
    public DeadlineNotValidException (LocalDateTime deadline) {
        super (String.format ("The deadline %s has already passed",deadline));
    }
}
class TaskManager {
    Map<String, List<Task>> tasksByCategory;

    public TaskManager () {
        this.tasksByCategory = new HashMap<> ();
    }

    void readTasks (InputStream inputStream) throws IOException {
        BufferedReader br = new BufferedReader (new InputStreamReader (inputStream));

        br.lines ().forEach (line -> {
            String category = TaskCreator.getCategory (line);
            try {
                Task task = TaskCreator.createTask (line);
                tasksByCategory.putIfAbsent (category, new ArrayList<> ());
                tasksByCategory.computeIfPresent (category, (k, v) -> {
                    v.add (task);
                    return v;
                });
            } catch (DeadlineNotValidException e) {
                System.out.println (e.getMessage ());
            }
        });
    }
    void printTasks(OutputStream os, boolean includePriority, boolean includeCategory){
        PrintWriter pw = new PrintWriter (os);

        Comparator<Task> priorityAndDeadline = Comparator.comparing (Task::getPriority)
                .thenComparing (Task::getTimeLeft);
        Comparator<Task> deadlineCompatator = Comparator.comparing (Task::getTimeLeft);

        Comparator<Task> taskComparator = includePriority ? priorityAndDeadline : deadlineCompatator;
        if (includeCategory){
            tasksByCategory.forEach ((category,tasks)->{
                pw.println (category.toUpperCase ());
                tasks.stream ()
                        .sorted (taskComparator)
                        .forEach (pw::println);
            });
        }
        else {
            tasksByCategory.values ()
                    .stream ()
                    .flatMap (Collection::stream)
                    .sorted (taskComparator)
                    .forEach (pw::println);
        }
        pw.flush ();

    }
}
    public class TasksManagerTest {

        public static void main(String[] args) throws IOException {

            TaskManager manager = new TaskManager();

            System.out.println("Tasks reading");
            manager.readTasks(System.in);
            System.out.println("By categories with priority");
            manager.printTasks(System.out, true, true);
            System.out.println("-------------------------");
            System.out.println("By categories without priority");
            manager.printTasks(System.out, false, true);
            System.out.println("-------------------------");
            System.out.println("All tasks without priority");
            manager.printTasks(System.out, false, false);
            System.out.println("-------------------------");
            System.out.println("All tasks with priority");
            manager.printTasks(System.out, true, false);
            System.out.println("-------------------------");

        }
    }
