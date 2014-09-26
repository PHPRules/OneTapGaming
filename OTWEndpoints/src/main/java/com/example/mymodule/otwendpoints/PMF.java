package com.example.mymodule.otwendpoints;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManagerFactory;


/**
 * Created by Jason on 8/12/2014.
 */
public final class PMF {
    private static final PersistenceManagerFactory pmfInstance =
            JDOHelper.getPersistenceManagerFactory("transactions-optional");

    private PMF() {}

    public static PersistenceManagerFactory get() {
        return pmfInstance;
    }
}

