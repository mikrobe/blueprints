package com.tinkerpop.blueprints.util.io.graphson;


import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.tg.TinkerGraph;
import com.tinkerpop.blueprints.impls.tg.TinkerGraphFactory;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GraphSONReaderTest {

    @Test
    public void inputGraphModeExtended() throws IOException {
        TinkerGraph graph = new TinkerGraph();

        String json = "{ \"mode\":\"EXTENDED\", \"vertices\": [ {\"_id\":1, \"_type\":\"vertex\", \"test\": { \"type\":\"string\", \"value\":\"please work\"}, \"testlist\":{\"type\":\"list\", \"value\":[{\"type\":\"int\", \"value\":1}, {\"type\":\"int\",\"value\":2}, {\"type\":\"int\",\"value\":3}, {\"type\":\"unknown\",\"value\":null}]}, \"testmap\":{\"type\":\"map\", \"value\":{\"big\":{\"type\":\"long\", \"value\":10000000000}, \"small\":{\"type\":\"double\", \"value\":0.4954959595959}, \"nullKey\":{\"type\":\"unknown\", \"value\":null}}}}, {\"_id\":2, \"_type\":\"vertex\", \"testagain\":{\"type\":\"string\", \"value\":\"please work again\"}}], \"edges\":[{\"_id\":100, \"_type\":\"edge\", \"_outV\":1, \"_inV\":2, \"_label\":\"works\", \"teste\": {\"type\":\"string\", \"value\":\"please worke\"}, \"keyNull\":{\"type\":\"unknown\", \"value\":null}}]}";

        byte[] bytes = json.getBytes();
        InputStream inputStream = new ByteArrayInputStream(bytes);

        GraphSONReader.inputGraph(graph, inputStream);

        Assert.assertEquals(2, getIterableCount(graph.getVertices()));
        Assert.assertEquals(1, getIterableCount(graph.getEdges()));

        Vertex v1 = graph.getVertex(1);
        Assert.assertNotNull(v1);
        Assert.assertEquals("please work", v1.getProperty("test"));

        Map map = (Map) v1.getProperty("testmap");
        Assert.assertNotNull(map);
        Assert.assertEquals(10000000000l, Long.parseLong(map.get("big").toString()));
        Assert.assertEquals(0.4954959595959, Double.parseDouble(map.get("small").toString()), 0);
        Assert.assertNull(map.get("nullKey"));

        List list = (List) v1.getProperty("testlist");
        Assert.assertEquals(4, list.size());

        boolean foundNull = false;
        for (int ix = 0; ix < list.size(); ix++) {
            if (list.get(ix) == null) {
                foundNull = true;
                break;
            }
        }

        Assert.assertTrue(foundNull);

        Vertex v2 = graph.getVertex(2);
        Assert.assertNotNull(v2);
        Assert.assertEquals("please work again", v2.getProperty("testagain"));

        Edge e = graph.getEdge(100);
        Assert.assertNotNull(e);
        Assert.assertEquals("works", e.getLabel());
        Assert.assertEquals(v1, e.getVertex(Direction.OUT));
        Assert.assertEquals(v2, e.getVertex(Direction.IN));
        Assert.assertEquals("please worke", e.getProperty("teste"));
        Assert.assertNull(e.getProperty("keyNull"));

    }

    @Test
    public void inputGraphModeNormal() throws IOException {
        TinkerGraph graph = new TinkerGraph();

        String json = "{ \"mode\":\"NORMAL\",\"vertices\": [ {\"_id\":1, \"_type\":\"vertex\", \"test\": \"please work\", \"testlist\":[1, 2, 3, null], \"testmap\":{\"big\":10000000000, \"small\":0.4954959595959, \"nullKey\":null}}, {\"_id\":2, \"_type\":\"vertex\", \"testagain\":\"please work again\"}], \"edges\":[{\"_id\":100, \"_type\":\"edge\", \"_outV\":1, \"_inV\":2, \"_label\":\"works\", \"teste\": \"please worke\", \"keyNull\":null}]}";

        byte[] bytes = json.getBytes();
        InputStream inputStream = new ByteArrayInputStream(bytes);

        GraphSONReader.inputGraph(graph, inputStream);

        Assert.assertEquals(2, getIterableCount(graph.getVertices()));
        Assert.assertEquals(1, getIterableCount(graph.getEdges()));

        Vertex v1 = graph.getVertex(1);
        Assert.assertNotNull(v1);
        Assert.assertEquals("please work", v1.getProperty("test"));

        Map map = (Map) v1.getProperty("testmap");
        Assert.assertNotNull(map);
        Assert.assertEquals(10000000000l, Long.parseLong(map.get("big").toString()));
        Assert.assertEquals(0.4954959595959, Double.parseDouble(map.get("small").toString()), 0);
        Assert.assertNull(map.get("nullKey"));

        List list = (List) v1.getProperty("testlist");
        Assert.assertEquals(4, list.size());

        boolean foundNull = false;
        for (int ix = 0; ix < list.size(); ix++) {
            if (list.get(ix) == null) {
                foundNull = true;
                break;
            }
        }

        Assert.assertTrue(foundNull);

        Vertex v2 = graph.getVertex(2);
        Assert.assertNotNull(v2);
        Assert.assertEquals("please work again", v2.getProperty("testagain"));

        Edge e = graph.getEdge(100);
        Assert.assertNotNull(e);
        Assert.assertEquals("works", e.getLabel());
        Assert.assertEquals(v1, e.getVertex(Direction.OUT));
        Assert.assertEquals(v2, e.getVertex(Direction.IN));
        Assert.assertEquals("please worke", e.getProperty("teste"));
        Assert.assertNull(e.getProperty("keyNull"));

    }

    @Test
    public void inputGraphModeCompact() throws IOException {
        TinkerGraph graph = new TinkerGraph();

        String json = "{ \"mode\":\"COMPACT\",\"vertices\": [ {\"_id\":1, \"test\": \"please work\", \"testlist\":[1, 2, 3, null], \"testmap\":{\"big\":10000000000, \"small\":0.4954959595959, \"nullKey\":null}}, {\"_id\":2, \"testagain\":\"please work again\"}], \"edges\":[{\"_id\":100, \"_outV\":1, \"_inV\":2, \"_label\":\"works\", \"teste\": \"please worke\", \"keyNull\":null}]}";

        byte[] bytes = json.getBytes();
        InputStream inputStream = new ByteArrayInputStream(bytes);

        GraphSONReader.inputGraph(graph, inputStream);

        Assert.assertEquals(2, getIterableCount(graph.getVertices()));
        Assert.assertEquals(1, getIterableCount(graph.getEdges()));

        Vertex v1 = graph.getVertex(1);
        Assert.assertNotNull(v1);
        Assert.assertEquals("please work", v1.getProperty("test"));

        Map map = (Map) v1.getProperty("testmap");
        Assert.assertNotNull(map);
        Assert.assertEquals(10000000000l, Long.parseLong(map.get("big").toString()));
        Assert.assertEquals(0.4954959595959, Double.parseDouble(map.get("small").toString()), 0);
        Assert.assertNull(map.get("nullKey"));

        List list = (List) v1.getProperty("testlist");
        Assert.assertEquals(4, list.size());

        boolean foundNull = false;
        for (int ix = 0; ix < list.size(); ix++) {
            if (list.get(ix) == null) {
                foundNull = true;
                break;
            }
        }

        Assert.assertTrue(foundNull);

        Vertex v2 = graph.getVertex(2);
        Assert.assertNotNull(v2);
        Assert.assertEquals("please work again", v2.getProperty("testagain"));

        Edge e = graph.getEdge(100);
        Assert.assertNotNull(e);
        Assert.assertEquals("works", e.getLabel());
        Assert.assertEquals(v1, e.getVertex(Direction.OUT));
        Assert.assertEquals(v2, e.getVertex(Direction.IN));
        Assert.assertEquals("please worke", e.getProperty("teste"));
        Assert.assertNull(e.getProperty("keyNull"));

    }

    @Test
    public void inputGraphExtendedFullCycle() throws IOException {
        TinkerGraph graph = TinkerGraphFactory.createTinkerGraph();

        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        GraphSONWriter writer = new GraphSONWriter(graph);
        writer.outputGraph(stream, null, null, GraphSONMode.EXTENDED);

        stream.flush();
        stream.close();

        String jsonString = new String(stream.toByteArray());

        byte[] bytes = jsonString.getBytes();
        InputStream inputStream = new ByteArrayInputStream(bytes);

        TinkerGraph emptyGraph = new TinkerGraph();
        GraphSONReader.inputGraph(emptyGraph, inputStream);

        Assert.assertEquals(6, getIterableCount(emptyGraph.getVertices()));
        Assert.assertEquals(6, getIterableCount(emptyGraph.getEdges()));

        for (Vertex v : graph.getVertices()) {
            Vertex found = emptyGraph.getVertex(v.getId());

            Assert.assertNotNull(v);

            for (String key : found.getPropertyKeys()) {
                Assert.assertEquals(v.getProperty(key), found.getProperty(key));
            }
        }

        for (Edge e : graph.getEdges()) {
            Edge found = emptyGraph.getEdge(e.getId());

            Assert.assertNotNull(e);

            for (String key : found.getPropertyKeys()) {
                Assert.assertEquals(e.getProperty(key), found.getProperty(key));
            }
        }

    }

    @Test
    public void inputGraphCompactFullCycle() throws IOException {
        TinkerGraph graph = TinkerGraphFactory.createTinkerGraph();

        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        Set<String> edgeKeys = new HashSet<String>();
        edgeKeys.add(GraphSONTokens._ID);
        edgeKeys.add(GraphSONTokens._IN_V);
        edgeKeys.add(GraphSONTokens._OUT_V);
        edgeKeys.add(GraphSONTokens._LABEL);

        Set<String> vertexKeys = new HashSet<String>();
        vertexKeys.add(GraphSONTokens._ID);

        GraphSONWriter writer = new GraphSONWriter(graph);
        writer.outputGraph(stream, edgeKeys, vertexKeys, GraphSONMode.COMPACT);

        stream.flush();
        stream.close();

        String jsonString = new String(stream.toByteArray());

        byte[] bytes = jsonString.getBytes();
        InputStream inputStream = new ByteArrayInputStream(bytes);

        TinkerGraph emptyGraph = new TinkerGraph();
        GraphSONReader.inputGraph(emptyGraph, inputStream);

        Assert.assertEquals(6, getIterableCount(emptyGraph.getVertices()));
        Assert.assertEquals(6, getIterableCount(emptyGraph.getEdges()));

        for (Vertex v : graph.getVertices()) {
            Vertex found = emptyGraph.getVertex(v.getId());

            Assert.assertNotNull(v);

            for (String key : found.getPropertyKeys()) {
                Assert.assertEquals(v.getProperty(key), found.getProperty(key));
            }

            // no properties should be here
            Assert.assertEquals(null, found.getProperty("name"));
        }

        for (Edge e : graph.getEdges()) {
            Edge found = emptyGraph.getEdge(e.getId());

            Assert.assertNotNull(e);

            for (String key : found.getPropertyKeys()) {
                Assert.assertEquals(e.getProperty(key), found.getProperty(key));
            }

            // no properties should be here
            Assert.assertEquals(null, found.getProperty("weight"));
        }

    }

    @Test(expected = IllegalArgumentException.class)
    public void inputGraphCompactFullCycleBroken() throws IOException {
        TinkerGraph graph = TinkerGraphFactory.createTinkerGraph();

        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        Set<String> edgeKeys = new HashSet<String>();
        edgeKeys.add(GraphSONTokens._IN_V);
        edgeKeys.add(GraphSONTokens._OUT_V);
        edgeKeys.add(GraphSONTokens._LABEL);

        Set<String> vertexKeys = new HashSet<String>();

        GraphSONWriter writer = new GraphSONWriter(graph);
        writer.outputGraph(stream, edgeKeys, vertexKeys, GraphSONMode.COMPACT);

        stream.flush();
        stream.close();

        String jsonString = new String(stream.toByteArray());

        byte[] bytes = jsonString.getBytes();
        InputStream inputStream = new ByteArrayInputStream(bytes);

        TinkerGraph emptyGraph = new TinkerGraph();
        GraphSONReader.inputGraph(emptyGraph, inputStream);

    }


    private int getIterableCount(Iterable elements) {
        int counter = 0;

        Iterator iterator = elements.iterator();
        while (iterator.hasNext()) {
            iterator.next();
            counter++;
        }

        return counter;
    }
}
