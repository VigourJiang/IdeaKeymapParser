import com.google.common.base.Joiner;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Sets;
import com.thoughtworks.xstream.XStream;
import entity.*;

import java.io.File;
import java.net.URL;
import java.util.Set;

/**
 * Created by vigour on 2014-8-13.
 */
public class Parser {
    public static void main(String[] args) {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        Parser defaultParser = new Parser();
        Parser emacsParser = new Parser();

        {
            URL url = loader.getResource("Keymap_Emacs.xml");
            emacsParser.parse(url.getPath());
        }
        {
            URL url2 = loader.getResource("Keymap_Default.xml");
            defaultParser.parse(url2.getPath());
        }

        for (String key : emacsParser.map.keySet()) {
            System.out.println(key + "\t" +
                    Joiner.on("|").join(emacsParser.map.get(key)));
        }

        System.out.println("_______");

        Set<String> diffKeys = Sets.difference(defaultParser.map.keySet(),
                emacsParser.map.keySet());
        for (String key : diffKeys) {
            System.out.println(key + "\t" +
                    Joiner.on("|").join(defaultParser.map.get(key)));
        }

    }

    private LinkedListMultimap<String, String> map = LinkedListMultimap.create();

    public void parse(String path) {
        XStream stream = new XStream();
        stream.processAnnotations(Component.class);

        File f = new File(path);
        Component c = (Component) stream.fromXML(f);
        traverse(c);
        System.out.println(map);
    }


    private void traverse(KeyShortcut shortcut, String actionId) {
        if (shortcut == null) {
            return;
        }
        map.put(actionId, shortcut.getKeyStroke());
    }

    private void traverse(MouseShortcut shortcut, String actionId) {
        if (shortcut == null) {
            return;
        }
        map.put(actionId, shortcut.getKeyStroke());
    }

    private void traverse(Keymap keymap) {
        if (keymap == null) {
            return;
        }
        for (Action action : keymap.getActions()) {
            traverse(action);
        }
    }

    private void traverse(Component component) {
        if (component == null) {
            return;
        }
        for (Keymap keymap : component.getKeymaps()) {
            traverse(keymap);
        }
    }

    private void traverse(Action action) {
        if (action == null) {
            return;
        }
        if (action.getKeyShortcut() != null) {
            for (KeyShortcut shortcut : action.getKeyShortcut()) {
                traverse(shortcut, action.getId());
            }
        }
        if (action.getMouseShortcut() != null) {
            for (MouseShortcut shortcut : action.getMouseShortcut()) {
                traverse(shortcut, action.getId());
            }
        }
    }
}

