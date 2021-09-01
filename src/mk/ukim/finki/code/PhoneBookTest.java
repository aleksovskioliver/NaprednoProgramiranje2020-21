package mk.ukim.finki.code;

import java.util.*;

class DuplicateNumberException extends Exception{
    public DuplicateNumberException(String message) {
        super(message);
    }
}
class Contact implements Comparable<Contact>{
    String name;
    String number;

    public Contact(String name, String number) {
        this.name = name;
        this.number = number;
    }

    @Override
    public int compareTo(Contact o) {
        int x = this.name.compareTo(o.name);
        if (x==0)
            return this.number.compareTo(o.number);
        return x;
    }

    @Override
    public String toString() {
        //Ashley 076522077
        return String.format("%s %s",name,number);
    }
}
class PhoneBook{
    //Map<Number,Contact>
    //Map<subNumber,Set<Contact>
    //Map<Name,Set<Contact>>
    Map<String,String> contactByNumber;
    Map<String, Set<Contact>> contactsBySubnumber;
    Map<String ,Set<Contact>> contactsByName;

    public PhoneBook(){
        this.contactByNumber = new HashMap<>();
        this.contactsBySubnumber = new HashMap<>();
        this.contactsByName = new HashMap<>();
    }
    public void addContact(String name,String number) throws DuplicateNumberException {
        if (contactByNumber.containsKey(number))
            throw new DuplicateNumberException(String.format("Duplicate number: %s",number));
        Contact c = new Contact(name,number);
        contactByNumber.put(number,name);

        contactsByName.putIfAbsent(name,new TreeSet<>());
        contactsByName.get(name).add(c);
//        contactsByName.computeIfPresent(name,(k,v)->{
//            v.add(c);
//            return v;
//        });
        for (String s : findSubNumbers(number)){
            this.contactsBySubnumber.putIfAbsent(s,new TreeSet<>());
            this.contactsBySubnumber.get(s).add(c);
//            this.contactsBySubnumber.computeIfPresent(s,(k,v)->{
//                v.add(c);
//                return v;
//            });
        }
    }
    void contactsByNumber(String number){
       if (!contactsBySubnumber.containsKey(number))
           System.out.println("NOT FOUND");
       else
           contactsBySubnumber.get(number).forEach(System.out::println);
    }
    void contactsByName(String name){
        if (!contactsByName.containsKey(name))
            System.out.println("NOT FOUND");
        else
            contactsByName.get(name).forEach(System.out::println);
    }
    private static List<String > findSubNumbers(String number){
        List<String > result = new ArrayList<>();
        for (int i=3;i<=number.length();i++){
            for (int j=0;j<=number.length()-i;j++){
                result.add(number.substring(j,j+i));
            }
        }
        return result;
    }
}

public class PhoneBookTest {

    public static void main(String[] args) {
        PhoneBook phoneBook = new PhoneBook();
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(":");
            try {
                phoneBook.addContact(parts[0], parts[1]);
            } catch (DuplicateNumberException e) {
                System.out.println(e.getMessage());
            }
        }
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            System.out.println(line);
            String[] parts = line.split(":");
            if (parts[0].equals("NUM")) {
                phoneBook.contactsByNumber(parts[1]);
            } else {
                phoneBook.contactsByName(parts[1]);
            }
        }
    }

}

// Вашиот код овде

