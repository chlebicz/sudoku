package pl.komponentowe.view;

import java.util.ListResourceBundle;

/**
 * Resource bundle containing author names.
 */
public class Authors extends ListResourceBundle {
    @Override
    protected Object[][] getContents() {
        return new Object[][] {
            {"authors", new String[]{
                "Mikołaj Chlebicz",
                "Bartosz Horna"
            } }
        };
    }
}
