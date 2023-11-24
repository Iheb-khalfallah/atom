/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package net.thevpc.gaming.atom.examples.kombla.main.client.dal;

/**
 *
 * @author iheb_kh
 */

import java.util.Map;

public class DefaultSprite {
    private int id;
    private String kind;
    private String name;
    private double x;
    private double y;
    private double direction;
    private int playerId;
    private Map<String, String> properties;

    public DefaultSprite(int id, String kind, String name, double x, double y, double direction, int playerId, Map<String, String> properties) {
        this.id = id;
        this.kind = kind;
        this.name = name;
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.playerId = playerId;
        this.properties = properties;
    }
}
