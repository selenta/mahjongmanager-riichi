package com.mahjongmanager.riichi.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * A custom Set for Tile objects, guarantees only one copy of any tile
 */
public class TileSet implements Iterable<Tile> {
    private List<Tile> tileSet = new ArrayList<>();

    public TileSet(){}
    public TileSet(List<Tile> list){
        addAll(list);
    }


    public boolean add(Tile tile) {
        if( contains(tile) ){
            return false;
        }
        tileSet.add(tile);
        return true;
    }

    public boolean addAll(Collection<Tile> collection) {
        boolean anythingChanged = false;
        for(Tile t : collection){
            if(!contains(t)){
                anythingChanged = true;
                tileSet.add(t);
            }
        }
        return anythingChanged;
    }

    public void clear() {
        tileSet.clear();
    }

    public boolean contains(Tile tile) {
        for(Tile t : tileSet){
            if( tile.isSame(t) ){
                return true;
            }
        }
        return false;
    }

    public Tile get(int location) {
        return tileSet.get(location);
    }

    public boolean isEmpty() {
        return tileSet.isEmpty();
    }

    public Tile remove(int location) {
        return tileSet.remove(location);
    }

    public boolean remove(Tile tile) {
        Tile doomed = null;
        for(Tile t : tileSet){
            if( tile.isSame(t) ){
                doomed = t;
            }
        }
        return tileSet.remove(doomed);
    }

    public boolean removeAll(Collection<Tile> collection) {
        boolean anythingRemoved = false;
        for( Tile tile : collection ){
            if( remove(tile) ){
                anythingRemoved = true;
            }
        }
        return anythingRemoved;
    }

    public int size() {
        return tileSet.size();
    }

    public List<Tile> subList(int start, int end) {
        return tileSet.subList(start, end);
    }

//    public List<Tile> toList(){
//        return new ArrayList<>(tileSet);
//    }

    @Override
    public Iterator<Tile> iterator() {
        return tileSet.iterator();
    }
}
