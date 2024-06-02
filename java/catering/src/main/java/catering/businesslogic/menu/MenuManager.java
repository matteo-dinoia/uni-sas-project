package catering.businesslogic.menu;

import catering.businesslogic.CatERing;
import catering.businesslogic.UseCaseLogicException;
import catering.businesslogic.recipe.Recipe;
import catering.businesslogic.user.User;
import javafx.collections.ObservableList;

public class MenuManager {
    private String[] menuFeatures = {"Richiede cucina", "Richiede cuoco", "Finger food", "Buffet", "Piatti caldi"};
    private Menu currentMenu;
    private MenuEventReceiver eventReceiver;

    public Menu createMenu() throws UseCaseLogicException {
        return this.createMenu(null);
    }

    public Menu createMenu(String title) throws UseCaseLogicException {
        User user = CatERing.getInstance().getUserManager().getCurrentUser();

        if (!user.isChef())
            throw new UseCaseLogicException();

        Menu m = new Menu(user, title, menuFeatures);
        this.setCurrentMenu(m);
        eventReceiver.updateMenuCreated(m);

        return m;
    }

    public Section defineSection(String name) throws UseCaseLogicException {
        if (currentMenu == null)
            throw new UseCaseLogicException();

        Section sec = this.currentMenu.addSection(name);

        eventReceiver.updateSectionAdded(this.currentMenu, sec);

        return sec;
    }

    public MenuItem insertItem(Recipe recipe, Section sec, String desc) throws UseCaseLogicException {
        if (this.currentMenu == null) throw new UseCaseLogicException();
        if (sec != null && this.currentMenu.getSectionPosition(sec) < 0) throw new UseCaseLogicException();
        MenuItem mi = this.currentMenu.addItem(recipe, sec, desc);
        eventReceiver.updateMenuItemAdded(this.currentMenu, mi);
        return mi;
    }

    public MenuItem insertItem(Recipe recipe, Section sec) throws UseCaseLogicException {
        return this.insertItem(recipe, sec, recipe.getName());
    }

    public MenuItem insertItem(Recipe rec) throws UseCaseLogicException {
        return this.insertItem(rec, null, rec.getName());
    }

    public MenuItem insertItem(Recipe rec, String desc) throws UseCaseLogicException {
        return this.insertItem(rec, null, desc);
    }

    public void setAdditionalFeatures(String[] features, boolean[] values) throws UseCaseLogicException {
        if (this.currentMenu == null) throw new UseCaseLogicException();
        if (features.length != values.length) throw new UseCaseLogicException();
        for (int i = 0; i < features.length; i++) {
            this.currentMenu.setFeatureValue(features[i], values[i]);
        }
        eventReceiver.updateMenuFeaturesChanged(this.currentMenu);
    }

    public void changeTitle(String title) throws UseCaseLogicException {
        if (currentMenu == null) throw new UseCaseLogicException();
        currentMenu.setTitle(title);
        eventReceiver.updateMenuTitleChanged(this.currentMenu);
    }

    public void publish() throws UseCaseLogicException {
        if (currentMenu == null) throw new UseCaseLogicException();
        currentMenu.setPublished(true);
        eventReceiver.updateMenuPublishedState(this.currentMenu);
    }

    public void deleteMenu(Menu m) throws UseCaseLogicException, MenuException {
        User u = CatERing.getInstance().getUserManager().getCurrentUser();
        if (!u.isChef()) throw new UseCaseLogicException();
        if (m.isInUse() || !m.isOwner(u)) {
            throw new MenuException();
        }
        eventReceiver.updateMenuDeleted(m);
    }

    public void chooseMenu(Menu m) throws UseCaseLogicException, MenuException {
        User u = CatERing.getInstance().getUserManager().getCurrentUser();
        if (!u.isChef()) throw new UseCaseLogicException();
        if (m.isInUse() || !m.isOwner(u)) {
            throw new MenuException();
        }
        this.currentMenu = m;
    }

    public Menu copyMenu(Menu toCopy) throws UseCaseLogicException {
        User user = CatERing.getInstance().getUserManager().getCurrentUser();

        if (!user.isChef()) {
            throw new UseCaseLogicException();
        }

        Menu m = new Menu(user, toCopy);
        this.setCurrentMenu(m);
        eventReceiver.updateMenuCreated(m);

        return m;
    }

    public void deleteSection(Section s, boolean deleteItems) throws UseCaseLogicException {
        if (currentMenu == null || currentMenu.getSectionPosition(s) < 0) throw new UseCaseLogicException();
        this.currentMenu.removeSection(s, deleteItems);

        eventReceiver.updateSectionDeleted(this.currentMenu, s, deleteItems);
    }

    public void changeSectionName(Section s, String name) throws UseCaseLogicException {
        if (currentMenu == null || currentMenu.getSectionPosition(s) < 0) throw new UseCaseLogicException();
        s.setName(name);

        eventReceiver.updateSectionChangedName(this.currentMenu, s);
    }

    public void moveSection(Section sec, int position) throws UseCaseLogicException {
        if (currentMenu == null || currentMenu.getSectionPosition(sec) < 0) throw new UseCaseLogicException();
        if (position < 0 || position >= currentMenu.getSectionCount()) throw new IllegalArgumentException();
        this.currentMenu.moveSection(sec, position);

        eventReceiver.updateSectionsRearranged(this.currentMenu);
    }

    public void moveMenuItem(MenuItem it, int position) throws UseCaseLogicException {
        this.moveMenuItem(it, null, position);
    }

    public void moveMenuItem(MenuItem mi, Section sec, int position) throws UseCaseLogicException {
        if (sec == null) {
            if (currentMenu == null || currentMenu.getFreeItemPosition(mi) < 0) throw new UseCaseLogicException();
            if (position < 0 || position >= currentMenu.getFreeItemCount()) throw new IllegalArgumentException();
            currentMenu.moveFreeItem(mi, position);
            eventReceiver.updateFreeItemsRearranged(this.currentMenu);
        } else {
            if (currentMenu == null || currentMenu.getSectionPosition(sec) < 0 || sec.getItemPosition(mi) < 0)
                throw new UseCaseLogicException();
            if (position < 0 || position >= sec.getItemsCount()) throw new IllegalArgumentException();
            sec.moveItem(mi, position);
            eventReceiver.updateSectionItemsRearranged(this.currentMenu, sec);
        }
    }

    public void assignItemToSection(MenuItem it) throws UseCaseLogicException {
        this.assignItemToSection(it, null);
    }

    public void assignItemToSection(MenuItem mi, Section sec) throws UseCaseLogicException {
        // condiz 1: deve eserci un currentMenu
        if (currentMenu == null) throw new UseCaseLogicException();

        // condiz 2: la sezione se specificata deve appartenere al current menu
        if (sec != null && currentMenu.getSectionPosition(sec) < 0) throw new UseCaseLogicException();

        // l'item deve appartenere al menu, o in una sezione o come voce libera
        Section oldsec = currentMenu.getSectionForItem(mi);
        if (oldsec == null && currentMenu.getFreeItemPosition(mi) < 0) throw  new UseCaseLogicException();

        // spostamento non necessario
        if (sec == oldsec) return;

        // spostiamo!
        this.currentMenu.changeItemSection(mi, oldsec, sec);

        eventReceiver.updateItemSectionChanged(this.currentMenu, sec, mi);

    }

    public void editMenuItemDescription(MenuItem mi, String desc) throws UseCaseLogicException {
        if (currentMenu == null) throw new UseCaseLogicException();
        if (currentMenu.getSectionForItem(mi) == null && currentMenu.getFreeItemPosition(mi) < 0) throw new UseCaseLogicException();

        mi.setDescription(desc);

        eventReceiver.updateItemDescriptionChanged(this.currentMenu, mi);
    }

    public void deleteItem(MenuItem mi) throws  UseCaseLogicException {
        if (currentMenu == null) throw new UseCaseLogicException();
        Section sec;
        try {
            sec = currentMenu.getSectionForItem(mi);
        } catch (IllegalArgumentException ex) {
            // se il menu mi dice che l'item non è valido
            // allora vuol dire che esso non appartiene al menu
            throw new UseCaseLogicException();
        }
        currentMenu.removeItem(mi);
        eventReceiver.updateItemDeleted(this.currentMenu, sec, mi);
    }

    public void setCurrentMenu(Menu m) {
        this.currentMenu = m;
    }

    public Menu getCurrentMenu() {
        return this.currentMenu;
    }

    public ObservableList<Menu> getAllMenus() {
        return Menu.loadAllMenus();
    }

    public void setEventReceiver(MenuEventReceiver rec) {
        this.eventReceiver = rec;
    }
}
