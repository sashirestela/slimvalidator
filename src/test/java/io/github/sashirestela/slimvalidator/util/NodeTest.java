package io.github.sashirestela.slimvalidator.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NodeTest {

    @Test
    void shouldReturnFullPathWhenNestedNodesAreBuilt() {
        var rootNode = new Node();
        var grandChildNode = rootNode.child("parent").child("child").child("grandChild");
        var actualFullPath = grandChildNode.toString();
        var expectedFullPath = "parent.child.grandChild";
        assertEquals(expectedFullPath, actualFullPath);
    }

}
