package domaincontrol;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Vector;

public class Triangle extends Board {
    @Override
    public void calculateAdjacencyMatrix(Vector<Vector<String>> matrix,String adjacency) {
        System.out.print("\n A CALCULAR MATRIU ADJACENCIES TRIANGLE");
        calculateBounds(matrix);
        /*switch (adjacency) {
            case "C":
                calculateBounds(matrix);
                break;

            case "CA":
                calculateBoundsVertexs(matrix);
                break;
        }*/
    }

    public void calculateBounds(Vector<Vector<String>> matrix) {
        //MATRIX.SIZE() * MATRIX.GET(0).SIZE() == TOTAL OF CELLS
        int lines = matrix.size();
        int columns = matrix.get(0).size();
        int total = lines * columns;
        Integer x1, x2, x3, x4, leftbound, rightbound;
        String value;
        for (int i = 0; i < lines; ++i) {
            leftbound = i * columns;
            rightbound = leftbound + columns - 1;
            Vector<String> vec = matrix.elementAt(i);
            for (int j = 0; j < columns; ++j) {
                value = vec.elementAt(j);
                Integer actual = (i * columns) + j;
                insertCell(actual,value);
                completeCellPositions(value,actual);
                ArrayList<Integer> aux = new ArrayList<>();
                x1 = actual - 1;
                x2 = actual + 1;
                x3 = actual + columns;
                x4 = actual - columns;
                if (x1 >= 0 && !actual.equals(leftbound)) aux.add(x1);
                if (x2 < total && !actual.equals(rightbound)) aux.add(x2);
                if (((i%2 == 0 && j%2 == 0) || (i%2 != 0 && j%2 != 0)) && x3 < total) aux.add(x3);
                if (((i%2 == 0 && j%2 != 0) || (i%2 != 0 && j%2 == 0)) && x4 >= 0) aux.add(x4);
                Collections.sort(aux);
                adjacencyMatrix.put(actual, aux);
            }
        }
        fillCellPositions();
    }

}
