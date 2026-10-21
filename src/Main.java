import java.util.Scanner;
import java.util.ArrayList;
import java.io.PrintWriter;
import java.io.File;

// Asks for floor and apartment number, checks them,
// and returns the apartment's position in the list (-1 if invalid).
int askIndex(Scanner input, int floors, int floorApartments)
{
    int floor = readInt(input, "Floor: ");
    int aptNb = readInt(input, "Apartment number: ");

    if (floor < 1 || floor > floors || aptNb < 1 || aptNb > floorApartments)
    {
        System.out.println("Invalid apartment. Floors go from 1 to " + floors
                + ", apartments from 1 to " + floorApartments + ".");
        return -1;
    }
    return (floor - 1) * floorApartments + (aptNb - 1);
}

        // Rewrites building.txt from scratch with the current data.
// Line 1: name;floors;apartmentsPerFloor
// Other lines: floor;number;tenantName;paid
        void save(Building b, int floors, int floorApartments) throws Exception
        {
            PrintWriter out = new PrintWriter("building.txt");
            out.println(b.name + ";" + floors + ";" + floorApartments);
            for (Apartment apt : b.apartments)
            {
                out.println(apt.floor + ";" + apt.number + ";" + apt.tenantName + ";" + apt.paid);
            }
            out.close();
        }

        // Asks until the user types a whole number that is at least 1.
        int readPositive(Scanner input, String question)
        {
            int number = readInt(input, question);
            while (number < 1)
            {
                System.out.println("Must be at least 1.");
                number = readInt(input, question);
            }
            return number;
        }

        // Asks until the name is not empty and has no ';' (';' is the file separator).
        String readName(Scanner input, String question)
        {
            System.out.print(question);
            String name = input.nextLine();
            while (name.contains(";") || name.trim().isEmpty())
            {
                System.out.print("Name can't be empty or contain ';': ");
                name = input.nextLine();
            }
            return name;
        }

        // Asks until the user types a whole number.
        int readInt(Scanner input, String question)
        {
            System.out.print(question);
            while (!input.hasNextInt())
            {
                input.nextLine();
                System.out.println("Not valid.");
                System.out.print(question);
            }
            int number = input.nextInt();
            input.nextLine(); // eat the leftover Enter
            return number;
        }

        void main() throws Exception
        {
            Building b = new Building();
            Scanner input = new Scanner(System.in);
            File f = new File("building.txt");
            int floors;
            int floorApartments;

            // ----- Load (file exists) or Setup (first run) -----
            if (f.exists())
            {
                Scanner fileScanner = new Scanner(f);
                String firstLine = fileScanner.nextLine();
                String[] parts = firstLine.split(";");
                b.name = parts[0];
                floors = Integer.parseInt(parts[1]);
                floorApartments = Integer.parseInt(parts[2]);

                while (fileScanner.hasNextLine())
                {
                    String line = fileScanner.nextLine();
                    parts = line.split(";");
                    Apartment apt = new Apartment();
                    apt.floor = Integer.parseInt(parts[0]);
                    apt.number = Integer.parseInt(parts[1]);
                    apt.tenantName = parts[2];
                    apt.paid = Boolean.parseBoolean(parts[3]);
                    b.apartments.add(apt);
                }
                fileScanner.close();
            }
            else
            {
                b.name = readName(input, "Building name: ");
                floors = readPositive(input, "Number of floors: ");
                floorApartments = readPositive(input, "Apartments per floor: ");

                System.out.println("\n" + b.name + ": " + floors + " floors, " + floorApartments + " apartments per floor.");
                System.out.println("Enter the tenant of each apartment:\n");

                for (int i = 0; i < floors; i++)
                {
                    for (int j = 0; j < floorApartments; j++)
                    {
                        Apartment apt = new Apartment();
                        apt.floor = i + 1;
                        apt.number = j + 1;
                        apt.tenantName = readName(input, "Floor " + apt.floor + ", apartment " + apt.number + " - tenant name: ");
                        apt.paid = false;
                        b.apartments.add(apt);
                    }
                }
                System.out.println("\n" + b.name + " has " + b.apartments.size() + " apartments.");
                save(b, floors, floorApartments);
            }

            // ----- Menu -----
            String choice = "";
            while (!choice.equals("x"))
            {
                System.out.println("\n===== MENU =====");
                System.out.println("p - record a payment");
                System.out.println("c - change tenant");
                System.out.println("u - show unpaid apartments");
                System.out.println("r - reset month");
                System.out.println("x - exit");
                System.out.print("Your choice: ");
                choice = input.nextLine().trim().toLowerCase();

                if (choice.equals("p"))
                {
                    int index = askIndex(input, floors, floorApartments);
                    if (index == -1)
                    {
                        continue;
                    }
                    Apartment apt = b.apartments.get(index);
                    System.out.print("Did " + apt.tenantName + " pay? (yes/no): ");
                    String payment = input.nextLine().trim().toLowerCase();
                    if (payment.equals("yes"))
                    {
                        apt.pay();
                        System.out.println("Payment recorded for " + apt.tenantName + ".");
                    }
                    else if (payment.equals("no"))
                    {
                        apt.paid = false;
                        System.out.println(apt.tenantName + " is marked as unpaid.");
                    }
                    else
                    {
                        System.out.println("Please answer yes or no. Nothing was changed.");
                    }
                    save(b, floors, floorApartments);
                }
                else if (choice.equals("u"))
                {
                    ArrayList<Apartment> unpaid = new ArrayList<>();
                    for (Apartment apt : b.apartments)
                    {
                        if (!apt.paid)
                        {
                            unpaid.add(apt);
                        }
                    }
                    if (unpaid.size() == 0)
                    {
                        System.out.println("Everyone has paid.");
                    }
                    else
                    {
                        System.out.println("Unpaid apartments (" + unpaid.size() + "):");
                        for (Apartment apt : unpaid)
                        {
                            System.out.println("  - Floor " + apt.floor + ", apartment " + apt.number + ": " + apt.tenantName);
                        }
                    }
                }
                else if (choice.equals("c"))
                {
                    int index = askIndex(input, floors, floorApartments);
                    if (index == -1)
                    {
                        continue;
                    }
                    Apartment apt = b.apartments.get(index);
                    String newName = readName(input, "New tenant name (current: " + apt.tenantName + "): ");
                    apt.changeTenant(newName);
                    System.out.println("Floor " + apt.floor + ", apartment " + apt.number + " tenant is now " + apt.tenantName + ".");
                    save(b, floors, floorApartments);
                }
                else if (choice.equals("r"))
                {
                    System.out.print("Are you sure? (yes/no): ");
                    String confirmation = input.nextLine().trim().toLowerCase();
                    if (confirmation.equals("yes"))
                    {
                        for (Apartment apt : b.apartments)
                        {
                            apt.paid = false;
                        }
                        save(b, floors, floorApartments);
                        System.out.println("Month reset. Everyone is unpaid.");
                    }
                    else
                    {
                        System.out.println("Cancelled.");
                    }
                }
                else if (choice.equals("x"))
                {
                    System.out.println("Goodbye!");
                }
                else
                {
                    System.out.println("Unknown choice. Type p, c, u, r or x.");
                }
            }
        }