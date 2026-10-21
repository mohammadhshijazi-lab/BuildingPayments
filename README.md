# BuildingPayments

A Java console app that tracks monthly rent payments for an apartment building.
Built as a learning project: every line was written by me.

## Features
- Setup once: building name, number of floors, apartments per floor, tenant per apartment
- Record a payment (paid / not paid) for any apartment
- Show all unpaid apartments
- Change a tenant
- Reset the month (with a yes/no confirmation)
- Input validation: bad numbers, invalid apartments, empty names or names with `;`
- Data is saved automatically to `building.txt` and loaded on the next run

## How to run
1. Install JDK 25 (the project uses an instance `main` method).
2. Open the project in IntelliJ IDEA and run `Main`.
3. First run: answer the setup questions. Next runs load the saved data.

To start over with a new building, delete `building.txt`.

## Project structure
- `Main.java` - menu, input helpers, save and load
- `Apartment.java` - one apartment (floor, number, tenant, paid)
- `Building.java` - the building name and its list of apartments

## Data file format
`building.txt` is plain text:

    Al Narjes;7;3
    1;1;Sara;true
    1;2;Omar;false

First line: `name;floors;apartmentsPerFloor`.
Other lines: `floor;number;tenantName;paid`.

## What I learned
Classes and objects, `ArrayList`, `Scanner` input handling, validation,
file reading and writing, and testing with redirected input.

## Ideas for later
- Support for several buildings
- Keep the previous month's history
- Handle a damaged `building.txt`
