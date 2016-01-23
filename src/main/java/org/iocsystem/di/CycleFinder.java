package org.iocsystem.di;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class CycleFinder {

    private final Map<Class, Vertex> vertexMap;
    private final List<Class> visitPath = new LinkedList<>();

    public CycleFinder(Map<Class, ModuleMetadata> dependencyMap) {
        this.vertexMap = buildVertexMap(dependencyMap);
    }

    public void find() throws CycleFinderException {
        for (Vertex vertex : vertexMap.values()) {
            visit(vertex);
            resetVisits();
        }
    }

    private void visit(Vertex vertex) throws CycleFinderException {
        visitPath.add(vertex.getClazz());
        if (!vertex.getDependencies().isEmpty()) {
            if (vertex.visited) {
                throw new CycleFinderException("Cyclic reference detected!. Cyclic path: " + visitPath);
            }
            vertex.visited = true;
            for (Class dependency : vertex.getDependencies()) {
                visit(vertexMap.get(dependency));
            }
        }
    }

    private void resetVisits() {
        for (Vertex vertex : vertexMap.values()) {
            vertex.visited = false;
        }
        visitPath.clear();
    }

    private static Map<Class, Vertex> buildVertexMap(Map<Class, ModuleMetadata> dependencyMap) {
        Map<Class, Vertex> vertexMap = new HashMap<>();
        for (Map.Entry<Class, ModuleMetadata> entry : dependencyMap.entrySet()) {
            vertexMap.put(entry.getKey(), new Vertex(entry.getValue()));
        }
        return vertexMap;
    }

    private static final class Vertex extends ModuleMetadata {
        boolean visited;

        Vertex(ModuleMetadata moduleMetadata) {
            super(moduleMetadata.getConstructor(), moduleMetadata.getDependencies());
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            Vertex that = (Vertex) obj;
            if (getConstructor() != null ? !getConstructor().equals(that.getConstructor()) :
                    that.getConstructor() != null) {
                return false;
            }
            boolean superEquals = !(getDependencies() != null ? !getDependencies().equals(that.getDependencies())
                    : that.getDependencies() != null);
            return superEquals && visited == that.visited;
        }

        @Override
        public int hashCode() {
            int result = getConstructor().hashCode();
            result = 31 * result + getDependencies().hashCode();
            result = 31 * result + (visited ? 1 : 0);
            return result;
        }
    }
}
