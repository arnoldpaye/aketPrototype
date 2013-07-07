package com.apt.bean;

import com.apt.aket.model.Text;
import com.apt.aket.model.Word;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

/**
 * Project: aketPrototype
 * Package: com.apt.bean
 * Class  : DataBean.java (UTF-8)
 * @date 06/07/2013
 * @author Arnold Paye
 */

@ManagedBean
@RequestScoped
public class DataBean {
    public List<Word> getWords() {
        List<Word> words = new ArrayList<Word>();
        words.add(new Word("Palabra 1", 0.5));
        words.add(new Word("Palabra 2", 0.5));
        words.add(new Word("Palabra 3", 0.5));
        words.add(new Word("Palabra 4", 0.5));
        words.add(new Word("Palabra 5", 0.5));
        words.add(new Word("Palabra 6", 0.5));
        return words;
    }
    
    public List<Text> getTexts() {
        List<Text> texts = new ArrayList<Text>();
        texts.add(new Text(1, "Seleccion automatizada de palabras clave mediante el modelo TextRank", "Arnold Paye", "---"));
        texts.add(new Text(2, "Voip sobre redes inalambricas aplicado a la telefonia movil", "Efrain Rodriquez", "---"));
        texts.add(new Text(3, "Patrones de identificacion biometrica mediante geometrica de la palma de la mano", "Guadalupe Antelo", "---"));
        texts.add(new Text(4, "Reconocimiento de texto manuscrito continuo en lengua aymaraa", "Ramiro Balmaceda", "---"));
        texts.add(new Text(5, "Diseno, validacion y verificacion de formularios, utilizando maquinas de Turing", "Hernan Valero", "---"));
        texts.add(new Text(6, "Sistema tutor inteligente para la ensenanza en ninos con discapacidad intelectual", "Gary Mamani", "---"));
        return texts;
    }
}
