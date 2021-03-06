package drivers.driverPresentationCtrl;

import drivers.driverPresentationCtrl.StubDomainControl;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.Vector;

public class DriverPresentationCtrl {
    private Vector<Vector<String>>  hidato= new Vector<>();
    private Character celltype;
    private String adjacencytype;
    private int lines;
    private int columns;

    //NEEDED FOR GENERATOR
    private int holes;
    private int predefined;

    private String username;

    private void extract_data(String input) {
        List<String> data = Arrays.asList(input.split(","));
        celltype = data.get(0).charAt(0);
        adjacencytype = data.get(1);
        lines = Integer.parseInt(data.get(2));
        columns = Integer.parseInt(data.get(3));
    }

    private Vector<String> extract_line(String input) {
        Vector<String> aux = new Vector<>();
        List<String> data = Arrays.asList(input.split(","));
        for(int i = 0; i < columns; ++i) {
            aux.add(data.get(i));
        }
        return aux;
    }

    private void extract_data_generator(String input) {
        List<String> data = Arrays.asList(input.split(","));
        celltype = data.get(0).charAt(0);
        adjacencytype = data.get(1);
        lines = Integer.parseInt(data.get(2));
        columns = Integer.parseInt(data.get(3));
        holes = Integer.parseInt(data.get(4));
        predefined = Integer.parseInt(data.get(5));
    }

    private int matrix_generator(String input) {
        for(int i = 0; i < lines; ++i) {
            String auxiliar[] = new String[columns];
            Arrays.fill(auxiliar,"?");
            hidato.add(new Vector<String>(Arrays.asList(auxiliar)));
        }
        List<String> data = Arrays.asList(input.split(","));
        int hast = data.size();
        if(lines*columns < (hast + holes + predefined)) return 0;
        for(String actual : data) {
            int act = Integer.parseInt(actual);
            Vector<String> mod = hidato.get(act/lines);
            mod.set(act%lines,"#");
            hidato.set(act/lines,mod);
        }
        return 1;
    }

    public int newGame(int flag) {
        hidato = new Vector<>();
        Scanner scan = new Scanner(System.in);
        System.out.print("\nCell Type? [Q,H,T],Adjacency Type? [C,CA],Lines Number,Column Number\n THEN ALL THE LINES OF HIDATO \n");
        String input = "";
        while(input.length() == 0) input = scan.nextLine();
        extract_data(input);
        input = "";
        for(int i = 0; i < lines; ++i) {
            input = scan.nextLine();
            hidato.add(extract_line(input));
        }
        StubDomainControl dc = new StubDomainControl();
        dc.newGame(username);
        if(flag == 0) {
            if(dc.defineBoard(hidato,username,adjacencytype,celltype) != null) {
                System.out.print("\nL'Hidato té resolució");
                return 1;
            }
            else {
                System.out.print("\nL'Hidato NO té resolució");
                return 0;
            }
        }
        if(flag == 1) {
            hidato = dc.defineBoard(hidato,username,adjacencytype,celltype);
            if(hidato == null) {
                System.out.print("\n NO s'ha pogut generar l'hidato amb els paràmetres demanats");
                return 0;
            }
            else {
                System.out.print("\n");
                for(int i = 0; i < lines; ++i) {
                    Vector<String> v = hidato.get(i);
                    for(int j = 0; j < (columns-1); ++j) {
                        System.out.print(v.get(j) + ",");
                    }
                    System.out.print(v.get(columns-1));
                    System.out.print("\n");
                }
                return 1;
            }
        }
        return lines;
    }

    public int newGenerator() {
        hidato = new Vector<>();
        Scanner scan = new Scanner(System.in);
        System.out.print("\n Cell Type? [Q,H,T], Adjacency Type? [C,CA], Lines Number, Column Number, Hole Number, Predefined Number \n");
        String input = "";
        while(input.length() == 0) input = scan.nextLine();
        extract_data_generator(input);
        input = "";
        System.out.print("\n Now all the positions ( Separated with ',' ) that the Hidato will be unaccessible (STARTING AT 0) if any, -1");
        while(input.length() == 0) input = scan.nextLine();
        System.out.print("\n Generation can last up to 15 seconds");
        if(matrix_generator(input) != 0) {
            StubDomainControl dc = new StubDomainControl();
            Vector<Vector<String>> mat = dc.generateHidato(hidato,adjacencytype,celltype,holes,predefined);
            if(mat == null) {
                System.out.print("\n NO Possible");
                return 0;
            }
            else {
                System.out.print("\n");
                for(int i = 0; i < lines; ++i) {
                    Vector<String> v = mat.get(i);
                    for(int j = 0; j < (columns-1); ++j) {
                        System.out.print(v.get(j) + ",");
                    }
                    System.out.print(v.get(columns-1));
                    System.out.print("\n");
                }
                return 1;
            }
        }
        else {
            System.out.print("\n Comprova els paràmetres, segur que són correctes?");
            return 0;
        }
    }

    public void main2() {
        Integer decission;
        Scanner scan = new Scanner(System.in);
        System.out.println("TEST PRESENTATION CONTROL, Tria la opció: ");
        System.out.print("1 - Extract_data(Setter dels atributs 'celltype','adjacencytype','lines','columns' " +
                "\n2 - Extract_line  \n3 - Extract_data_generator (Setter dels atributs 'celltype','adjacencytype'," +
                "'lines','columns','holes','predefined' \n" +
                "4 - Matrix_generator\n" +
                "5 - NewGame(Validar Hidato) \n6 - NewGame (Resoldre Hidato) \n6 - NewGenerator \n0 - Sortir del sistema");
        decission = scan.nextInt();
        while(decission != 0) {
            switch (decission) {
                case 1:
                    System.out.println("Introdueix celltype,adjacencytype,lines,columns:\n");
                    String input = "";
                    while(input.length() == 0) input = scan.nextLine();
                    extract_data(input);
                    System.out.println(celltype + " " + adjacencytype + " " + lines + " " + columns);
                    break;

                case 2:
                    System.out.println("Introdueix linia d'hidato (separada per ','):");
                    String input2 = "";
                    while(input2.length() == 0) input2 = scan.nextLine();
                    Vector<String> res = extract_line(input2);
                    for(int i = 0; i < res.size(); ++i) {
                        System.out.print(res.elementAt(i));
                    }
                    break;

                case 3:
                    System.out.println("Introdueix linia d'hidato (separada per ','):");
                    String input3 = "";
                    while(input3.length() == 0) input3 = scan.nextLine();
                    extract_data_generator(input3);
                    System.out.println(celltype + " " + adjacencytype + " " + lines + " " + columns + " " + holes + " " + predefined);
                    break;

                case 4:
                    System.out.println("Put all the list of '*' you want to place, Starting at 0");
                    String input4 = "";
                    while(input4.length() == 0) input4 = scan.nextLine();
                    int res1 = matrix_generator(input4);
                    System.out.println(res1);
                    break;

                case 5:
                    newGame(0);
                    break;

                case 6:
                    newGame(1);
                    break;
                default:
                    break;
            }
            System.out.print("1 - Extract_data(Setter dels atributs 'celltype','adjacencytype','lines','columns' " +
                    "\n2 - Extract_line  \n3 - Extract_data_generator \n3 - Matrix_generator\n" +
                    "4 - NewGame \n5 - NewGenerator \n0 - Sortir del sistema");
            decission = scan.nextInt();
        }
        System.out.print("\nGràcies i fins una altra!");
    }

    public static void main(String[] args) {
        DriverPresentationCtrl dp = new DriverPresentationCtrl();
        dp.main2();
    }
}
