package com.github.imhammer.ihautoclicker.manager;

import java.util.Iterator;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;

public class FXPaneManager<T extends Node>
{
    private final T root;
    
    public FXPaneManager(T root)
    {
        this.root = root;
    }

    public T getRoot()
    {
        return this.root;
    }

    public <N extends Node> FXPaneManager<N> child(String fxId, Class<N> clazz)
    {
        if (this.root instanceof Pane) {
            N result = findNodeById(fxId, ((Pane)this.root).getChildren());
            if (result != null) {
                if (clazz.isInstance(result)) {
                    return new FXPaneManager<>(result);
                }
                throw new RuntimeException("Result has class %s, expected: %s".formatted(result.getClass().getName(), clazz.getName()));
            }
            throw new RuntimeException("Node with %s id not found!".formatted(fxId));
        }
        throw new RuntimeException("Root cannot start with %s class, you need a class extends Pane".formatted(this.root.getClass().getName()));
    }

    // LOW LEVEL NODES

    public <P extends Node> P node(String fxId, Class<P> clazz)
    {
        return this.<P>child(fxId, clazz).getRoot();
    }

    public <P extends Pane> FXPaneManager<P> pane(String fxId, Class<P> clazz)
    {
        return this.<P>child(fxId, clazz);
    }

    public <P extends ButtonBase> P buttonBase(String fxId, Class<P> clazz)
    {
        return this.<P>child(fxId, clazz).getRoot();
    }

    public <P extends TextInputControl> P textInputControl(String fxId, Class<P> clazz)
    {
        return this.<P>child(fxId, clazz).getRoot();
    }

    public <P extends Labeled> P labeled(String fxId, Class<P> clazz)
    {
        return this.<P>child(fxId, clazz).getRoot();
    }

    // HIGH LEVEL NODES

    public FXPaneManager<AnchorPane> anchorPane(String fxId)
    {
        return pane(fxId, AnchorPane.class);
    }

    public FXPaneManager<FlowPane> flowPane(String fxId)
    {
        return pane(fxId, FlowPane.class);
    }

    // ScrollPane não é um painel como Flow e Anchor, e sim um controle
    public ScrollPane scrollPane(String fxId)
    {
        // ScrollPane não extende a classe Pane
        return this.<ScrollPane>child(fxId, ScrollPane.class).getRoot();
    }

    public Label label(String fxId)
    {
        return labeled(fxId, Label.class);
    }

    public TextField textField(String fxId)
    {
        return textInputControl(fxId, TextField.class);
    }

    public Button button(String fxId)
    {
        return buttonBase(fxId, Button.class);
    }

    public CheckBox checkBox(String fxId)
    {
        return buttonBase(fxId, CheckBox.class);
    }

    public RadioButton radioButton(String fxId)
    {
        return buttonBase(fxId, RadioButton.class);
    }

    public ImageView image(String fxId)
    {
        return node(fxId, ImageView.class);
    }

    @SuppressWarnings("unchecked")
    private <J extends Node> J findNodeById(String id, ObservableList<Node> slapow)
    {
        Iterator<Node> iterator = slapow.iterator();
        while (iterator.hasNext()) {
            Node node = iterator.next();
            if (node.getId() != null && node.getId().equals(id)) {
                return (J) node;
            }
        }
        return null;
    }
}
