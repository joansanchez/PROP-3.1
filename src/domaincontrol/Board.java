package domaincontrol;

import java.util.*;

public abstract class Board {
    private static Integer MAXGENERATIONTRIES;
    protected Map<Integer, Integer> cellPositions;
    protected Vector<Cell> vectorCell;
    protected Map<Integer, ArrayList<Integer>> adjacencyMatrix;
    protected Integer counter;
    protected Utilities utils = new Utilities();
    protected Integer generationTries;

    protected Map<Integer, Integer> cellPositionsProposalResult;

    public Board() {
        cellPositions = new HashMap<>();
        vectorCell = new Vector<>();
        adjacencyMatrix = new HashMap<>();
        cellPositionsProposalResult = new HashMap<>();
        generationTries = 0;
    }

    public void createBoard(Vector<Vector<String>> matrix, String adjacency) {
        adjacencyMatrix = new HashMap<>();
        cellPositions = new HashMap<>();
        vectorCell = new Vector<>();
        counter = 0;
        calculateAdjacencyMatrix(matrix, adjacency);
        utils.printAdjacencyMatrix(adjacencyMatrix);
        utils.printCellPositions(cellPositions);
        utils.printCells(vectorCell);
    }


    //Getters and setters
    public void setCellPositions(Map<Integer, Integer> cellPositionsExterior) {
        cellPositions = cellPositionsExterior;
    }

    public Map<Integer, Integer> getCellPositionsProposalResult() {
        return cellPositionsProposalResult;
    }

    public void setVectorCell(Vector<Cell> vectorCellExterior) {
        vectorCell = vectorCellExterior;
    }

    public Vector<Cell> getVectorCell() {
        return vectorCell;
    }

    public void setAdjacencyMatrix(Map<Integer, ArrayList<Integer>> adjacencyMatrixExterior) {
        adjacencyMatrix = adjacencyMatrixExterior;
    }

    public Map<Integer, ArrayList<Integer>> getAdjacencyMatrix() {
        return adjacencyMatrix;
    }


    //solver

    public boolean solveHidato() {
        if (previousConditions()) return Solver();
        else return false;
    }

    private boolean previousConditions() {  //Check some of hamiltonian graphs conditions to know if there is a possible path
        boolean first = false;
        int how_many_1 = 0;
        Iterator<Map.Entry<Integer, ArrayList<Integer>>> it = adjacencyMatrix.entrySet().iterator();
        while (it.hasNext() && (how_many_1 < 3 || ((how_many_1 == 2) && (first == true)))) {
            Map.Entry<Integer, ArrayList<Integer>> pair = it.next();
            if (pair.getKey().equals(1)) {
                first = true;
            }
            if (pair.getValue().size() == 1) {
                ++how_many_1;
            }
        }
        if (how_many_1 < 3 || ((how_many_1 == 2) && (first == true))) return true;
        if (adjacencyMatrix.get(1).size() == 0) return false;
        else return false;
    }

    private boolean Solver() {
        Boolean[] already_visited = new Boolean[vectorCell.size()]; //vector of visited cells
        Arrays.fill(already_visited, Boolean.FALSE); //filled with false
        int position_cell_id_1 = cellPositions.get(1); //id de la celda del número 1
        return recursiveSolver(position_cell_id_1, already_visited, 1, cellPositions);

    }

    private boolean recursiveSolver(Integer cell_c0, Boolean[] already_visited_LevelUp, int number_c0, Map<Integer, Integer> cellPositionsLevelUp) {
        Map<Integer, Integer> cellPositionsRecursive = utils.copyMap(cellPositionsLevelUp);
        Boolean[] already_visited = utils.copyBoolean(already_visited_LevelUp);
        already_visited[cell_c0] = Boolean.TRUE;
        boolean route_found = false;
        ArrayList<Integer> adjacencies_cell_c0 = adjacencyMatrix.get(cell_c0); //id cell_c0 neighbours
        int position_cell_c1 = -1;
        //base case
        if (number_c0 == cellPositionsRecursive.size()) {
            cellPositionsProposalResult = utils.copyMap(cellPositionsRecursive);
            return true;
        }

        if (cellPositionsRecursive.containsKey(number_c0 + 1)) { //el número n+1 té celda assignada
            position_cell_c1 = cellPositionsRecursive.get(number_c0 + 1);
        }
        if (position_cell_c1 != -1) {
            boolean found_cell_c1 = false;
            Iterator<Integer> iterator = adjacencies_cell_c0.iterator();
            while (!found_cell_c1 && iterator.hasNext()) {
                int next_value = iterator.next();
                if (next_value == position_cell_c1) {
                    found_cell_c1 = true;
                    route_found = recursiveSolver(next_value, already_visited, number_c0 + 1, cellPositionsRecursive);
                }
            }
            if (found_cell_c1 == false) return false;
        } else { // el número n+1 no té assignada cap celda
            Iterator<Integer> iterator = adjacencies_cell_c0.iterator(); //adjacencies c0
            while (!route_found  && iterator.hasNext()) { //recorrent possibles c1
                int next_cell_c1 = iterator.next();
                if(!already_visited[next_cell_c1] && vectorCell.elementAt(next_cell_c1).getNumber() == -1){
                    cellPositionsRecursive.put(number_c0 + 1, next_cell_c1);
                    route_found = recursiveSolver(next_cell_c1, already_visited, number_c0 + 1, cellPositionsRecursive);
                    if(!route_found) cellPositionsRecursive.put(number_c0 + 1, -1);

                }
            }
        }
        //Todo tractament quan el hidato només té una celda
        return route_found;
    }


    //generator
    public abstract void calculateAdjacencyMatrix(Vector<Vector<String>> matrix, String adjcency);

    public void completeCellPositions(String value, Integer actual) {
        if (!value.equals("#") && !value.equals("*")) {
            if (value.equals("?")) ++counter;
            else cellPositions.put(Integer.parseInt(value), actual);
        }
    }

    public void fillCellPositions() {
        System.out.print("\n Fill cell positions");
        Integer total = cellPositions.size() + counter;
        for (int i = 1; i <= total; ++i) {
            if (!cellPositions.containsKey(i)) cellPositions.put(i, -1);
        }
        Map<Integer, Integer> sorted = new TreeMap<Integer, Integer>(cellPositions);
        cellPositions = sorted;
    }

    public void insertCell(int id, String value) {
        Cell c;
        switch (value) {
            case "#":
                c = new Cell(id, false, -3);
                break;

            case "*":
                c = new Cell(id, false, -2);
                break;

            case "?":
                c = new Cell(id, true, -1);
                break;

            default:
                c = new Cell(id, true, Integer.valueOf(value));
                break;
        }
        vectorCell.add(c);
    }

    public boolean accesible(String value) {
        return (!value.equals("#") && !value.equals("*"));
    }



    public int generateHidato(Vector<Vector<String>> matrix, int maxColumns, String adjacency, int holes, int toshow) {
        adjacencyMatrix = new HashMap<>();
        cellPositions = new HashMap<>();
        vectorCell = new Vector<>();
        counter = 0;
        calculateAdjacencyMatrix(matrix, adjacency);

        System.out.print("\n Matriu adjacencies feta");
        int numberCells = matrix.size()*maxColumns;

        if (numberCells < 10) MAXGENERATIONTRIES = 30;
        else if (numberCells > 10 && numberCells < 50) MAXGENERATIONTRIES = 20;
        else MAXGENERATIONTRIES = 10;

        //placing element 1
        int resultCode = setElement1(matrix, maxColumns);
        if (resultCode == 0) return 0;
        System.out.print("\n Nombre 1 posicionat");
        //remove up to "holes"
        int holesSet = 0;
        removeLastHoles(holes, holesSet, toshow);
        return 1;
    }

    private void removeLastHoles(int holes, int holesSet, int toShow) {
        Vector<Integer> lastPositions = new Vector<>();
        if (holesSet < holes) {
            lastPositions = utils.MapToVector(cellPositionsProposalResult);
        }
        while (holesSet < holes) {
            int size = cellPositionsProposalResult.size();
            int cellVector = lastPositions.elementAt(size - 1); //no está accediendo a la posición última, sino a la key size
            Cell temporalCell = vectorCell.get(cellVector);
            temporalCell.setAccessible(false);
            temporalCell.setNumber(-2);
            cellPositionsProposalResult.remove(size);
            lastPositions.remove(lastPositions.size() - 1);
            ++holesSet;
        }

        ShowOnlyAskedFilledPositions(toShow);
    }

    private void ShowOnlyAskedFilledPositions(int toShow) {
        Vector<Integer> toErase = new Vector<>();
        for (int i = 2; i <= cellPositionsProposalResult.size(); ++i) {
            toErase.add(i);
        }
        Collections.shuffle(toErase);
        for (int i = toShow - 1; i < toErase.size(); ++i) {
            cellPositionsProposalResult.replace(toErase.elementAt(i), -1);
        }
        for (int i = 0; i < toShow - 1; ++i) {
            Cell temporalCell = vectorCell.get(cellPositionsProposalResult.get(toErase.elementAt(i)));
            temporalCell.setAccessible(true);
            temporalCell.setNumber(toErase.elementAt(i));
        }
    }

    private Integer setElement1(Vector<Vector<String>> matrix, int maxColumns) {
        boolean onePlaced = false;
        int idCell;
        while (!onePlaced) {
            ++generationTries;
            if (generationTries > MAXGENERATIONTRIES) return 0;

            idCell = utils.getRandomNumber(0, (matrix.size() * maxColumns) - 1);
            if (vectorCell.elementAt(idCell).getNumber() == -1) {
                changeVectorCell(idCell,1);
                changeCellPositions(1,idCell);
                if (solveHidato()){
                    onePlaced = true;
                }
                else {
                    changeVectorCell(idCell,-1);
                    changeCellPositions(1,-1);
                    onePlaced = false;
                }
            }
        }
        return 1;
    }

    private void changeCellPositions(int positionSource, int idCell) {
        cellPositions.replace(positionSource, idCell);
    }

    private void changeVectorCell(int idCell, int i) {
        Cell temporalCell = vectorCell.get(idCell);
        temporalCell.setNumber(i);
    }



}
