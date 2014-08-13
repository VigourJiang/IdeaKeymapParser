package entity;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.List;

/**
 * Created by vigour on 2014-8-13.
 */
@XStreamAlias("component")
public class Component {
    @XStreamImplicit(itemFieldName = "keymap")
    private List<Keymap> keymaps;

    public List<Keymap> getKeymaps() {
        return keymaps;
    }

    public void setKeymaps(List<Keymap> keymaps) {
        this.keymaps = keymaps;
    }
}
