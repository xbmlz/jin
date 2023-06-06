package com.github.xbmlz.jin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Node {

    /**
     * eg: /user/:id
     */
    private String pattern;

    /**
     * eg: :id
     */
    private String part;

    /**
     * eg: [1, 2, 3]
     */
    private List<Node> children;

    /**
     * eg: part include : or *, isWild is true
     */
    private boolean isWild;

    private Map<String, String> params;

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public String getPart() {
        return part;
    }

    public void setPart(String part) {
        this.part = part;
    }

    public List<Node> getChildren() {
        return children;
    }

    public void setChildren(List<Node> children) {
        this.children = children;
    }

    public boolean isWild() {
        return isWild;
    }

    public void setWild(boolean wild) {
        isWild = wild;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public void insert(String pattern, List<String> parts, int height) {
        if (parts.size() == height) {
            this.pattern = pattern;
            return;
        }
        String part = parts.get(height);
        Node child = matchChild(part);
        if (child == null) {
            child = new Node();
            child.setPart(part);
            child.setWild(part.charAt(0) == ':' || part.charAt(0) == '*');
            this.children.add(child);
        }
        child.insert(pattern, parts, height + 1);
    }

    public Node search(List<String> parts, int height) {
        // TODO part has prefix '*'
        if (parts.size() == height || this.part.equals("*")) {
            if (this.pattern == null) {
                return null;
            }
            return this;
        }
        String part = parts.get(height);
        List<Node> children = matchChildren(part);
        for (Node child : children) {
            Node result = child.search(parts, height + 1);
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    public void travel(List<Node> nodes) {
        if (this.pattern != null) {
            nodes.add(this);
        }
        for (Node child : children) {
            child.travel(nodes);
        }
    }

    public List<Node> matchChildren(String part) {
        List<Node> nodes = new ArrayList<>();
        for (Node child : this.children) {
            if (child.part.equals(part) || child.isWild) {
                nodes.add(child);
            }
        }
        return nodes;
    }

    public Node matchChild(String part) {
        for (Node child : children) {
            if (child.part.equals(part) || child.isWild) {
                return child;
            }
        }
        return null;
    }
}
