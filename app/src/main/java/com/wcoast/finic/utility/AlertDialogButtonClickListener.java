package com.wcoast.finic.utility;

/**
 * Created by Deepak on 07/03/2018.
 * Interface to handle two button alert dialog.
 */

public interface AlertDialogButtonClickListener {

    /**
     * Method to be called on button one click
     */
    void onButtonOneClicked();

    /**
     * Method to be called on button two click
     */
    void onButtonTwoClicked();
}
