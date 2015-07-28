package com.huiyin.ui.store;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by justme on 15/5/9.
 */
public class StoreCategory {

    @Expose
    private String NAME;
    @Expose
    private int ID;
    @SerializedName("TWO_CATEGORY")
    @Expose
    private ArrayList<TwoCategory> TWOCATEGORY = new ArrayList<TwoCategory>();

    /**
     *
     * @return
     * The NAME
     */
    public String getNAME() {
        return NAME;
    }

    /**
     *
     * @param NAME
     * The NAME
     */
    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    /**
     *
     * @return
     * The ID
     */
    public int getID() {
        return ID;
    }

    /**
     *
     * @param ID
     * The ID
     */
    public void setID(int ID) {
        this.ID = ID;
    }

    /**
     *
     * @return
     * The TWOCATEGORY
     */
    public ArrayList<TwoCategory> getTWOCATEGORY() {
        return TWOCATEGORY;
    }

    /**
     *
     * @param TWOCATEGORY
     * The TWO_CATEGORY
     */
    public void setTWOCATEGORY(ArrayList<TwoCategory> TWOCATEGORY) {
        this.TWOCATEGORY = TWOCATEGORY;
    }

    public class TwoCategory {
        @Expose
        private String NAME;
        @Expose
        private int ID;

        /**
         *
         * @return
         * The NAME
         */
        public String getNAME() {
            return NAME;
        }

        /**
         *
         * @param NAME
         * The NAME
         */
        public void setNAME(String NAME) {
            this.NAME = NAME;
        }

        /**
         *
         * @return
         * The ID
         */
        public int getID() {
            return ID;
        }

        /**
         *
         * @param ID
         * The ID
         */
        public void setID(int ID) {
            this.ID = ID;
        }
    }
}
