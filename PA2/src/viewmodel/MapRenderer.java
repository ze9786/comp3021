package viewmodel;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import model.Map.Cell;
import model.Map.Occupant.Player;
import model.Map.Occupiable.DestTile;
import model.Map.Occupiable.Occupiable;

import java.net.URISyntaxException;
import static viewmodel.Config.LEVEL_EDITOR_TILE_SIZE;

/**
 * Renders maps onto canvases
 */
public class MapRenderer {
    private static Image wall = null;
    private static Image crateOnTile = null;
    private static Image crateOnDest = null;

    private static Image playerOnTile = null;
    private static Image playerOnDest = null;

    private static Image dest = null;
    private static Image tile = null;

    static {
        try {
            wall = new Image(MapRenderer.class.getResource("/assets/images/wall.png").toURI().toString());
            crateOnTile = new Image(MapRenderer.class.getResource("/assets/images/crateOnTile.png").toURI().toString());
            crateOnDest = new Image(MapRenderer.class.getResource("/assets/images/crateOnDest.png").toURI().toString());
            playerOnTile = new Image(MapRenderer.class.getResource("/assets/images/playerOnTile.png").toURI().toString());
            playerOnDest = new Image(MapRenderer.class.getResource("/assets/images/playerOnDest.png").toURI().toString());
            dest = new Image(MapRenderer.class.getResource("/assets/images/dest.png").toURI().toString());
            tile = new Image(MapRenderer.class.getResource("/assets/images/tile.png").toURI().toString());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    /**
     * Render the map onto the canvas. This method can be used in Level Editor
     * <p>
     * Hint: set the canvas height and width as a multiple of the rows and cols
     *
     * @param canvas The canvas to be rendered onto
     * @param map    The map holding the current state of the game
     */
    static void render(Canvas canvas, LevelEditorCanvas.Brush[][] map) {
        //TODO
        int numCols=map[0].length;
        int numRows=map.length;
        canvas.setHeight(numRows*LEVEL_EDITOR_TILE_SIZE);
        canvas.setWidth(numCols*LEVEL_EDITOR_TILE_SIZE);
        GraphicsContext gc=canvas.getGraphicsContext2D();
        for(int i=0; i<numRows; i++){
            for(int j=0; j<numCols; j++){
                if(map[i][j].toString().equals("Wall"))
                    gc.drawImage(wall, j*Config.LEVEL_EDITOR_TILE_SIZE, i*Config.LEVEL_EDITOR_TILE_SIZE);
                else if(map[i][j].toString().equals("Crate on Tile"))
                    gc.drawImage(crateOnTile, j*Config.LEVEL_EDITOR_TILE_SIZE, i*Config.LEVEL_EDITOR_TILE_SIZE);
                else if(map[i][j].toString().equals("Crate on Destination"))
                    gc.drawImage(crateOnDest, j*Config.LEVEL_EDITOR_TILE_SIZE, i*Config.LEVEL_EDITOR_TILE_SIZE);
                else if(map[i][j].toString().equals("Player on Tile"))
                    gc.drawImage(playerOnTile, j*Config.LEVEL_EDITOR_TILE_SIZE, i*Config.LEVEL_EDITOR_TILE_SIZE);
                else if(map[i][j].toString().equals("Player on Destination"))
                    gc.drawImage(playerOnDest, j*Config.LEVEL_EDITOR_TILE_SIZE, i*Config.LEVEL_EDITOR_TILE_SIZE);
                else if(map[i][j].toString().equals("Destination"))
                    gc.drawImage(dest, j*Config.LEVEL_EDITOR_TILE_SIZE, i*Config.LEVEL_EDITOR_TILE_SIZE);
                else
                    gc.drawImage(tile, j* Config.LEVEL_EDITOR_TILE_SIZE, i * Config.LEVEL_EDITOR_TILE_SIZE);
            }
        }
    }

    /**
     * Render the map onto the canvas. This method can be used in GamePlayPane and LevelSelectPane
     * <p>
     * Hint: set the canvas height and width as a multiple of the rows and cols
     *
     * @param canvas The canvas to be rendered onto
     * @param map    The map holding the current state of the game
     */
    public static void render(Canvas canvas, Cell[][] map) {
        //TODO
        int numCols=map[0].length;
        int numRows=map.length;
        canvas.setHeight(numRows*LEVEL_EDITOR_TILE_SIZE);
        canvas.setWidth(numCols*LEVEL_EDITOR_TILE_SIZE);
        GraphicsContext gc=canvas.getGraphicsContext2D();
        for(int i=0; i<numRows; i++){
            for(int j=0; j<numCols; j++){
                if(map[i][j] instanceof Occupiable){
                    Occupiable o=(Occupiable)map[i][j];
                    if(o.getOccupant().isPresent()) {
                        if (o.getOccupant().get() instanceof Player) {
                            if((Occupiable)map[i][j] instanceof DestTile)//??
                                 gc.drawImage(playerOnDest, j * Config.LEVEL_EDITOR_TILE_SIZE, i * Config.LEVEL_EDITOR_TILE_SIZE);
                            else
                                gc.drawImage(playerOnTile, j * Config.LEVEL_EDITOR_TILE_SIZE, i * Config.LEVEL_EDITOR_TILE_SIZE);
                        }
                        else if((Occupiable)map[i][j] instanceof DestTile)
                            gc.drawImage(crateOnDest, j * Config.LEVEL_EDITOR_TILE_SIZE, i * Config.LEVEL_EDITOR_TILE_SIZE);
                        else
                            gc.drawImage(crateOnTile, j * Config.LEVEL_EDITOR_TILE_SIZE, i * Config.LEVEL_EDITOR_TILE_SIZE);

                    }
                    else if((Occupiable)map[i][j] instanceof DestTile)
                        gc.drawImage(dest, j*Config.LEVEL_EDITOR_TILE_SIZE, i*Config.LEVEL_EDITOR_TILE_SIZE);
                    else
                        gc.drawImage(tile, j*Config.LEVEL_EDITOR_TILE_SIZE, i*Config.LEVEL_EDITOR_TILE_SIZE);
                }
                else
                    gc.drawImage(wall, j*Config.LEVEL_EDITOR_TILE_SIZE, i*Config.LEVEL_EDITOR_TILE_SIZE);
            }
        }
    }
}
