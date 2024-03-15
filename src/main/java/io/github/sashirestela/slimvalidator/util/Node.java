package io.github.sashirestela.slimvalidator.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Node {

    private Node parent;
    private String name;

    public Node(Node parent, String name) {
        this.parent = parent;
        this.name = name;
    }

    public Node() {
        this(null, null);
    }

    public Node child(String name) {
        return new Node(this, name);
    }

    public String toString() {
        return toList().stream().collect(Collectors.joining("."));
    }

    private List<String> toList() {
        Node node = this;
        List<String> path = new ArrayList<>();
        while (node.parent != null) {
            path.add(node.name);
            node = node.parent;
        }
        Collections.reverse(path);
        return path;
    }

}
