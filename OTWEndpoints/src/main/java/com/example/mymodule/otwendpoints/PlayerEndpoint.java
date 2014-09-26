package com.example.mymodule.otwendpoints;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
//import com.google.appengine.api.datastore.EntityNotFoundException;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import java.util.logging.Logger;

import javax.inject.Named;
import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;


import static javax.jdo.JDOHelper.getPersistenceManager;

/** An endpoint class we are exposing */
@Api(name = "playerEndpoint", version = "v1", namespace = @ApiNamespace(ownerDomain = "otwendpoints.onetapwinning.onetapgaming.com", ownerName = "otwendpoints.onetapwinning.onetapgaming.com", packagePath=""),
        clientIds = {Constants.WEB_CLIENT_ID, Constants.ANDROID_CLIENT_ID, com.google.api.server.spi.Constant.API_EXPLORER_CLIENT_ID},
        audiences = {Constants.WEB_CLIENT_ID},
        scopes = {Constants.EMAIL_SCOPE}
)
public class PlayerEndpoint {

    // Make sure to add this endpoint to your web.xml file if this is a web application.

    private static final Logger LOG = Logger.getLogger(PlayerEndpoint.class.getName());

    /**
     * This method gets the <code>Player</code> object associated with the specified <code>id</code>.
     * @param userID The id of the object to be returned.
     * @return The <code>Player</code> associated with <code>id</code>.
     */
    @ApiMethod(name = "getPlayer")
    public Player getPlayer(@Named("userID") String userID) {
        // Implement this function
        PersistenceManager mgr = getPersistenceManager();
        Player player = null;
        try
        {
            Key key = KeyFactory.createKey(Player.class.getSimpleName(), userID);
            player = mgr.getObjectById(Player.class, key);

        } finally {
            mgr.close();
        }
        LOG.info("Calling getPlayer method");
        return player;
    }

    /**
     * This inserts a new <code>Player</code> object.
     * @param player The object to be added.
     * @return The object to be added.
     */
    @ApiMethod(name = "insertPlayer")
    public Player insertPlayer(Player player) {
        PersistenceManager mgr = getPersistenceManager();
        try
        {
            if(player.getUserID() != null)
            {
                if(containsPlayer(player))
                {
                    throw new EntityExistsException("Object already exists!");
                }
            }
            mgr.makePersistent(player);
        } finally {
            mgr.close();
        }

        LOG.info("Calling insertPlayer method");
        return player;
    }

    @ApiMethod(name = "updatePlayer")
    public Player updatePlayer(Player player)
    {
        PersistenceManager mgr = getPersistenceManager();
        try{
            if(!containsPlayer(player))
            {
                throw new EntityNotFoundException("Object Not Found!");
            }
            mgr.makePersistent(player);
        }finally {
            mgr.close();
        }
        return player;
    }

    @ApiMethod(name = "removePlayer")
    public void removePlayer(@Named("userID") String userID)
    {
        PersistenceManager mgr = getPersistenceManager();
        try{
            Player player;
            Key key = KeyFactory.stringToKey(userID);
            player = mgr.getObjectById(Player.class, key);
            mgr.deletePersistent(player);
        }finally {
            mgr.close();
        }
    }

    private boolean containsPlayer(Player player)
    {
        PersistenceManager mgr = getPersistenceManager();
        boolean contains = true;
        try
        {
            Key key = KeyFactory.stringToKey(player.getUserID());
            mgr.getObjectById(Player.class, key);        }
        catch (JDOObjectNotFoundException ex)
        {
            contains =  false;
        }
        finally {
            mgr.close();
        }
        return contains;
    }
    private static PersistenceManager getPersistenceManager() {
        return PMF.get().getPersistenceManager();
    }
}