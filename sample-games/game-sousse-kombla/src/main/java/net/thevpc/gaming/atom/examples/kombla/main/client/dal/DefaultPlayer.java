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

public class DefaultPlayer {
    private int id;
    private String name;
    private Map<String, String> properties;

    public DefaultPlayer(int id, String name, Map<String, String> properties) {
        this.id = id;
        this.name = name;
        this.properties = properties;
    }
}
