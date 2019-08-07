/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package analisador;

import analisador.model.Dic;
import analisador.model.Lexico;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import java.util.regex.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 *
 * @author Rodolfo Barreiro
 */
public class FXMLDocumentController implements Initializable {

    @FXML
    private Label label;

    @FXML
    private AnchorPane anchorPaneMain;
    @FXML
    private TextArea AreaCode;
    @FXML
    private Button handleMenuAnalisar;
    @FXML
    private Button handleMenuCode;
    @FXML
    private Button handleMenuLimpar;

    @FXML
    private TableView<Lexico> tableView = new TableView<>();
    @FXML
    private TableColumn<Lexico, Integer> tableColumnToken;
    @FXML
    private TableColumn<Lexico, String> tableColumnClassificacao;
    @FXML
    private TableColumn<Lexico, String> tableColumnLinha;

    private List<Lexico> listCodes = new ArrayList<>();
    private ObservableList<Lexico> observableListCodes;

    private final String backgroung_pd = "-fx-background-color:#fff";

    private static final String[] reservadas = {"program", "var", "integer", "real", "boolean", "procedure", "begin", "end", "if", "then", "else", "while", "do", "not", "write", "readln", "read"};
    private static final String[] delimitador = {";", ".", ":", "(", ")", ","};
    private static final String[] atrb = {":="};
    private static final String[] opr_relacionais = {"=", "<", ">", "<=", ">=", "<>"};
    private static final String[] opr_add = {"+", "-", "or"};
    private static final String[] opr_mult = {"*", "/", "and"};
    private static final String[] tipo = {"integer", "real", "boolean"};
    private static final Pattern varValida = Pattern.compile("\\s*[a-z]_*[a-zA-Z_0-9]*");
    private static final Pattern varNotValida = Pattern.compile("\\s*[A-Z]_*[a-zA-Z_0-9]*");
    private static final Pattern natValida = Pattern.compile("\\s*[0-9]*");
    private static final Pattern realValida = Pattern.compile("\\s*[0-9]*[.][0-9]*");

    private final String[] verify = {";", ".", ":", "(", ")", ",", "=", "<", ">", "+", "-", "*", "/", "+", "-", "*"};
    private final String[] verifynot = {"=", ">"};
    private final String[] calc = {"+", "-", "*", "/"};

    /**
     * ************** EXTRAS ***********
     */////
    private final String[] verifybarra = {"//"};

    private static final Pattern tokenArray = Pattern.compile("\\s*[0-9]*[.][0-9]*[\\s*[eE]*][+][\\s*[0-9]*]");

    /**
     * ************** ***********
     */////
    Vector<Dic> palvraF;

    private int pLinha = 0;

    private static final String PALAVRA_RESERVADA = "Palavra reservada";

    /**
     * *************************************
     */
    @FXML
    public void handleMenuAnalisar(ActionEvent event) throws IOException {

        if (armazenaSimbolos(AreaCode.getText())) {
            working();
            carregarTableViewCliente();

            program();
        }

    }

    @FXML
    public void handleMenuSobre(ActionEvent event) throws IOException {
        AnchorPane a = (AnchorPane) FXMLLoader.load(getClass().getResource("/analisador/view/FXML_Relatorio.fxml"));
        anchorPaneMain.getChildren().setAll(a);
        handleMenuCode.setVisible(true);
        handleMenuAnalisar.setVisible(false);
        handleMenuLimpar.setVisible(false);

    }

    @FXML
    public void handleMenuLimpar(ActionEvent event) throws IOException {
        AreaCode.setText("");
        listCodes.clear();
        carregarTableViewCliente();

    }

    @FXML
    public void handleMenuLimparTabela(ActionEvent event) throws IOException {
        listCodes.clear();
        carregarTableViewCliente();

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    public void working() {

        for (int i = 0; i < palvraF.size(); i++) {

            if (Arrays.asList(reservadas).contains(palvraF.get(i).getSimbolo())) {
                Lexico e = new Lexico(palvraF.get(i).getSimbolo(), PALAVRA_RESERVADA, palvraF.get(i).getLinha(), backgroung_pd);
                palvraF.get(i).setClassificacao(PALAVRA_RESERVADA);
                listCodes.add(e);
            } else if (Arrays.asList(atrb).contains(palvraF.get(i).getSimbolo())) {
                Lexico e = new Lexico(palvraF.get(i).getSimbolo(), "Atribuição", palvraF.get(i).getLinha(), backgroung_pd);
                palvraF.get(i).setClassificacao("Atribuição");
                listCodes.add(e);
            } else if (Arrays.asList(opr_relacionais).contains(palvraF.get(i).getSimbolo())) {
                Lexico e = new Lexico(palvraF.get(i).getSimbolo(), "Operador relacional", palvraF.get(i).getLinha(), backgroung_pd);
                palvraF.get(i).setClassificacao("Operador relacional");
                listCodes.add(e);
            } else if (Arrays.asList(opr_add).contains(palvraF.get(i).getSimbolo())) {
                Lexico e = new Lexico(palvraF.get(i).getSimbolo(), "Operador aditivo", palvraF.get(i).getLinha(), backgroung_pd);
                palvraF.get(i).setClassificacao("Operador aditivo");
                listCodes.add(e);
            } else if (Arrays.asList(opr_mult).contains(palvraF.get(i).getSimbolo())) {
                Lexico e = new Lexico(palvraF.get(i).getSimbolo(), "Operador multiplicativo", palvraF.get(i).getLinha(), backgroung_pd);
                palvraF.get(i).setClassificacao("Operador multiplicativo");
                listCodes.add(e);
            } else if (Arrays.asList(delimitador).contains(palvraF.get(i).getSimbolo()) && !Arrays.asList(atrb).contains(palvraF.get(i).getSimbolo())) {
                Lexico e = new Lexico(palvraF.get(i).getSimbolo(), "Delimitador", palvraF.get(i).getLinha(), backgroung_pd);
                palvraF.get(i).setClassificacao("Delimitador");
                listCodes.add(e);
            } else if (varValida.matcher(palvraF.get(i).getSimbolo()).matches()) {
                Lexico e = new Lexico(palvraF.get(i).getSimbolo(), "Identificador", palvraF.get(i).getLinha(), backgroung_pd);
                palvraF.get(i).setClassificacao("Identificador");
                listCodes.add(e);
            } else if (natValida.matcher(palvraF.get(i).getSimbolo()).matches()) {
                Lexico e = new Lexico(palvraF.get(i).getSimbolo(), "Número inteiro", palvraF.get(i).getLinha(), backgroung_pd);
                palvraF.get(i).setClassificacao("Número inteiro");
                listCodes.add(e);
            } else if (realValida.matcher(palvraF.get(i).getSimbolo()).matches()) {
                Lexico e = new Lexico(palvraF.get(i).getSimbolo(), "Número real", palvraF.get(i).getLinha(), backgroung_pd);
                palvraF.get(i).setClassificacao("Número real");
                listCodes.add(e);
            } else if (tokenArray.matcher(palvraF.get(i).getSimbolo()).matches()) {
                Lexico e = new Lexico(palvraF.get(i).getSimbolo(), "POTENCIA", palvraF.get(i).getLinha(), backgroung_pd);
                palvraF.get(i).setClassificacao("POTENCIA");
                listCodes.add(e);
            } else if (palvraF.get(i).getSimbolo().equalsIgnoreCase(" ")) {
            } else if (palvraF.get(i).getSimbolo().equalsIgnoreCase("")) {
            } else if (palvraF.get(i).getSimbolo().equalsIgnoreCase("")) {
            } else {
                System.out.println(palvraF.get(i).getSimbolo());
                System.out.println(palvraF.get(i).getSimbolo());
                System.out.println("PARTE1 ERRO LINHA: " + i);
                System.out.println("_____________");
            }

        }

    }

    public void carregarTableViewCliente() {

        tableColumnToken.setCellValueFactory(new PropertyValueFactory<>("Token"));
        tableColumnClassificacao.setCellValueFactory(new PropertyValueFactory<>("classificacao"));
        tableColumnLinha.setCellValueFactory(new PropertyValueFactory<>("linha"));

        observableListCodes = FXCollections.observableArrayList(listCodes);
        tableView.setItems(observableListCodes);

    }

    public void program() {
        boolean programTrust = false;

        if (palvraF.get(pLinha).getSimbolo().equalsIgnoreCase("program")) {

            if (varValida.matcher(palvraF.get(++pLinha).getSimbolo()).matches()
                    && !palvraF.get(pLinha).getClassificacao().equalsIgnoreCase(PALAVRA_RESERVADA)) {

                if (palvraF.get(++pLinha).getSimbolo().equalsIgnoreCase(";")) {
                    programTrust = true;
                } else {
                    System.err.println("Fatal: erro de sintaxe, esperado um (;)  mas (" + palvraF.get(pLinha).getSimbolo() + ") encontrado");
                    programTrust = false;
                }
            } else {
                System.err.println("Fatal: erro de sintaxe, esperado um (identificador)  mas (" + palvraF.get(pLinha).getSimbolo() + ") encontrado");
                programTrust = false;
            }
        } else {
            System.err.println("Fatal: erro de sintaxe, esperado um (program)  mas (" + palvraF.get(pLinha).getSimbolo() + ") encontrado");
            programTrust = false;
        }
        if (programTrust) {
            if (var()) {

                while (palvraF.get(pLinha + 1).getSimbolo().equalsIgnoreCase("procedure")) {
                    programTrust = procedure();
                }

                do {

                    programTrust = begin();

                } while (palvraF.get(pLinha + 1).getSimbolo().equalsIgnoreCase("begin"));

                if (palvraF.get(++pLinha).getSimbolo().equalsIgnoreCase(".")) {
                    print("program CORRETO;");
                } else {
                    System.err.println("Fatal: erro de sintaxe, esperado um (.)  mas (" + palvraF.get(pLinha).getSimbolo() + ") encontrado " + palvraF.get(pLinha).getLinha());
                    programTrust = false;
                }
            }

        } else {
            System.out.println("program INCORRETO;");
        }

    }

    public boolean armazenaSimbolos(String linhas) {

        pLinha = 0;
        palvraF = new Vector();

        String linha[] = linhas.split("\\r?\\n");
        boolean coments = false;
        boolean comentsbarra = false;

        for (int i = 0; i < linha.length; i++) {

            String replaceAll = linha[i].trim().replaceAll("\t", "").replaceAll("\\s+", " ");

            comentsbarra = false;
            if (!"".equals(replaceAll)) {
                String tokens[] = replaceAll.split(" ");

                for (int j = 0; j < tokens.length; j++) {

                    if (tokens[j].equalsIgnoreCase("{")) {
                        coments = true;
                    } else if (tokens[j].equalsIgnoreCase("}")) {
                        coments = false;
                        // Falta tratar fim do arquivo
                    } else if (tokens[j].equalsIgnoreCase("//")) {
                        comentsbarra = true;
                        // Falta tratar fim do arquivo
                    } else if (!coments && !comentsbarra) {
                        if (!tokens[j].equalsIgnoreCase("")) {
                            palvraF.add(new Dic(tokens[j].trim(), null, null, null, i));
                        }

                    }
                }

            }

        }

        if (coments) {
            System.out.println("Comentario Aberto e Não Fechado: ");
            System.out.println("_____________");
        }

        return true;

    }

    public boolean var() {
        boolean varTrust = false;
        if (palvraF.get(++pLinha).getSimbolo().equalsIgnoreCase("var")) {
            if (varValida.matcher(palvraF.get(++pLinha).getSimbolo()).matches()
                    && !palvraF.get(pLinha).getClassificacao().equalsIgnoreCase(PALAVRA_RESERVADA)) {
                while (true) {
                    if (palvraF.get(++pLinha).getSimbolo().equalsIgnoreCase(":")) {
                        if (Arrays.asList(tipo).contains(palvraF.get(++pLinha).getSimbolo())) {
                            if (palvraF.get(++pLinha).getSimbolo().equalsIgnoreCase(";")) {
                                if (palvraF.get(pLinha + 1).getSimbolo().equalsIgnoreCase("begin") || palvraF.get(pLinha + 1).getSimbolo().equalsIgnoreCase("procedure")) {
                                    varTrust = true;
                                    break;
                                } else {
                                    if (varValida.matcher(palvraF.get(++pLinha).getSimbolo()).matches()
                                            && !palvraF.get(pLinha).getClassificacao().equalsIgnoreCase(PALAVRA_RESERVADA)) {

                                    } else {
                                        printErroSyn("begin ou procedure", palvraF.get(pLinha).getSimbolo());
                                        varTrust = false;
                                        break;
                                    }
                                }

                            } else {
                                printErroSyn(";", palvraF.get(pLinha).getSimbolo());
                                varTrust = false;
                                break;
                            }
                        } else {
                            printErroSyn("tipo", palvraF.get(pLinha).getSimbolo());
                            varTrust = false;
                            break;
                        }

                    } else if (palvraF.get(pLinha).getSimbolo().equalsIgnoreCase(",")) {
                        if (varValida.matcher(palvraF.get(++pLinha).getSimbolo()).matches()
                                && !palvraF.get(pLinha).getClassificacao().equalsIgnoreCase(PALAVRA_RESERVADA)) {

                        } else {
                            printErroSyn("identificador", palvraF.get(pLinha).getSimbolo());
                            varTrust = false;
                            break;
                        }

                    } else {
                        printErroSyn(":", palvraF.get(pLinha).getSimbolo());
                        varTrust = false;
                        break;

                    }

                }
            } else {
                printErroSyn("identificador", palvraF.get(pLinha).getSimbolo());
                varTrust = false;
            }

        } else {
            printErroSyn("var", palvraF.get(pLinha).getSimbolo());
            varTrust = false;
        }
        if (varTrust) {
            //print("VAR CORRETO;");
        } else {
            //print("VAR INCORRETO;");
        }
        return varTrust;
    }

    public boolean procedure() {
        boolean procedureTrust = false;

        if (palvraF.get(++pLinha).getSimbolo().equalsIgnoreCase("procedure")) {
            if (varValida.matcher(palvraF.get(++pLinha).getSimbolo()).matches()
                    && !palvraF.get(pLinha).getClassificacao().equalsIgnoreCase(PALAVRA_RESERVADA)) {
                if (palvraF.get(++pLinha).getSimbolo().equalsIgnoreCase("(")) {
                    if (argumentosProcedure()) {
                        if (palvraF.get(++pLinha).getSimbolo().equalsIgnoreCase("var")) {
                            pLinha--;
                            procedureTrust = var();
                            ++pLinha;

                        }

                        if (palvraF.get(pLinha).getSimbolo().equalsIgnoreCase("procedure")) {
                            pLinha--;

                            procedureTrust = procedure();

                            if (palvraF.get(pLinha + 1).getSimbolo().equalsIgnoreCase("begin")) {

                                procedureTrust = begin();

                                if (palvraF.get(++pLinha).getSimbolo().equalsIgnoreCase(";")) {
                                    procedureTrust = true;

                                } else {
                                    printErroSyn(";", palvraF.get(pLinha).getSimbolo());
                                    procedureTrust = false;
                                }

                            } else {
                                printErroSyn("begin", palvraF.get(pLinha).getSimbolo());
                                procedureTrust = false;
                            }

                        } else if (palvraF.get(pLinha).getSimbolo().equalsIgnoreCase("begin")) {
                            pLinha--;

                            procedureTrust = begin();

                            if (palvraF.get(++pLinha).getSimbolo().equalsIgnoreCase(";")) {
                                procedureTrust = true;

                            } else {
                                printErroSyn(";", palvraF.get(pLinha).getSimbolo());
                                procedureTrust = false;
                            }

                        } else {
                            printErroSyn("begin ou procedure", palvraF.get(pLinha).getSimbolo());
                            procedureTrust = false;
                        }
                    }

                } else {
                    printErroSyn("(", palvraF.get(pLinha).getSimbolo());
                    procedureTrust = false;
                }

            } else {
                printErroSyn("identificador", palvraF.get(pLinha).getSimbolo());
                procedureTrust = false;
            }
        } else {
            printErroSyn("begin ou procedure", palvraF.get(pLinha).getSimbolo());
            procedureTrust = false;
        }

        if (procedureTrust) {
            //print("PROCEDURE CORRETO;");
        } else {
            //print("PROCEDURE INCORRETO;");
        }

        return procedureTrust;
    }

    public boolean argumentosProcedure() {
        boolean varTrust = false;

        if (palvraF.get(++pLinha).getSimbolo().equalsIgnoreCase("var")) {
            varTrust = varProcedureArguments();
        } else if (varValida.matcher(palvraF.get(pLinha--).getSimbolo()).matches()
                && !palvraF.get(pLinha).getClassificacao().equalsIgnoreCase(PALAVRA_RESERVADA)) {
            varTrust = varProcedureArguments();
        } else {
            printErroSyn("var ou identificador", palvraF.get(pLinha).getSimbolo());
            varTrust = false;
        }

        if (varTrust) {
            //print("VAR_PROCEDURE CORRETO;");
        } else {
            //print("VAR_PROCEDURE INCORRETO;");
        }
        return varTrust;
    }

    public boolean varProcedureArguments() {
        boolean varTrust = false;
        if (varValida.matcher(palvraF.get(++pLinha).getSimbolo()).matches()
                && !palvraF.get(pLinha).getClassificacao().equalsIgnoreCase(PALAVRA_RESERVADA)) {
            while (true) {
                if (palvraF.get(++pLinha).getSimbolo().equalsIgnoreCase(":")) {
                    if (Arrays.asList(tipo).contains(palvraF.get(++pLinha).getSimbolo())) {
                        if (palvraF.get(++pLinha).getSimbolo().equalsIgnoreCase(";")) {

                            argumentosProcedure();
                            varTrust = true;
                            break;
                        } else if (palvraF.get(pLinha).getSimbolo().equalsIgnoreCase(")")) {
                            if (palvraF.get(++pLinha).getSimbolo().equalsIgnoreCase(";")) {
                                varTrust = true;
                                break;
                            } else {
                                printErroSyn(";", palvraF.get(pLinha).getSimbolo());
                                varTrust = false;
                                break;
                            }

                        } else {
                            printErroSyn(";", palvraF.get(pLinha).getSimbolo());
                            varTrust = false;
                            break;
                        }
                    } else {
                        printErroSyn("tipo", palvraF.get(pLinha).getSimbolo());
                        varTrust = false;
                        break;
                    }

                } else if (palvraF.get(pLinha).getSimbolo().equalsIgnoreCase(",")) {
                    if (varValida.matcher(palvraF.get(++pLinha).getSimbolo()).matches()
                            && !palvraF.get(pLinha).getClassificacao().equalsIgnoreCase(PALAVRA_RESERVADA)) {

                    } else {
                        printErroSyn("identificador", palvraF.get(pLinha).getSimbolo());
                        varTrust = false;
                        break;
                    }

                } else {
                    printErroSyn(":", palvraF.get(pLinha).getSimbolo());
                    varTrust = false;
                    break;

                }

            }
        } else {
            printErroSyn("identificador", palvraF.get(pLinha).getSimbolo());
            varTrust = false;
        }
        return varTrust;
    }

    public boolean begin() {
        boolean beginTrust = false;

        if (palvraF.get(++pLinha).getSimbolo().equalsIgnoreCase("begin")) {

            beginTrust = listaDeComandos();

            if (palvraF.get(++pLinha).getSimbolo().equalsIgnoreCase("end")) {
                beginTrust = true;
            } else {
                printErroSyn("end", palvraF.get(pLinha).getSimbolo());
                beginTrust = false;
            }
        } else {
            printErroSyn("begin", palvraF.get(pLinha).getSimbolo());
            beginTrust = false;
        }

        if (beginTrust) {
            //print("BEGIN CORRETO;");
        } else {
            //print("BEGIN INCORRETO;");
        }

        return beginTrust;
    }

    public boolean listaDeComandos() {
        boolean lista_de_comandos = true, listaComandos = false;

        while (lista_de_comandos) {
            if (varValida.matcher(palvraF.get(pLinha + 1).getSimbolo()).matches()
                    && !palvraF.get(pLinha + 1).getClassificacao().equalsIgnoreCase(PALAVRA_RESERVADA)) {
                ++pLinha;

                if (Arrays.asList(atrb).contains(palvraF.get(++pLinha).getSimbolo())) {

                    beginExpressao();

                    if (!palvraF.get(++pLinha).getSimbolo().equalsIgnoreCase(";")) {
                        printErroSyn(";", palvraF.get(pLinha).getSimbolo());
                        listaComandos = false;
                    }

                } else if (palvraF.get(pLinha).getSimbolo().equalsIgnoreCase("(")) {
                    varProcedureBeginArguments();
                    if (!palvraF.get(++pLinha).getSimbolo().equalsIgnoreCase(";")) {
                        printErroSyn(";", palvraF.get(pLinha).getSimbolo());
                        listaComandos = false;
                    }
                } else {
                    printErroSyn(":=", palvraF.get(pLinha).getSimbolo());
                    listaComandos = false;
                }

            } else if (palvraF.get(pLinha + 1).getSimbolo().equalsIgnoreCase("if")) {
                ++pLinha;
                if (palvraF.get(++pLinha).getSimbolo().equalsIgnoreCase("(")) {

                    beginExpressao();

                    if (!palvraF.get(++pLinha).getSimbolo().equalsIgnoreCase(")")) {
                        printErroSyn(")", palvraF.get(pLinha).getSimbolo());
                        listaComandos = false;
                    }

                    if (palvraF.get(++pLinha).getSimbolo().equalsIgnoreCase("then")) {

                        if (palvraF.get(pLinha + 1).getSimbolo().equalsIgnoreCase("begin")) {

                            listaComandos = begin();

                            if (palvraF.get(++pLinha).getSimbolo().equalsIgnoreCase(";")) {
                                listaComandos = true;

                            } else if (palvraF.get(pLinha).getSimbolo().equalsIgnoreCase("else")) {

                                if (palvraF.get(++pLinha).getSimbolo().equalsIgnoreCase(";")) {
                                    listaComandos = true;

                                } else if (palvraF.get(pLinha).getSimbolo().equalsIgnoreCase("begin")) {

                                    pLinha--;

                                    listaComandos = begin();

                                    if (palvraF.get(++pLinha).getSimbolo().equalsIgnoreCase(";")) {
                                        listaComandos = true;

                                    } else {
                                        printErroSyn(";", palvraF.get(pLinha).getSimbolo());
                                        listaComandos = false;
                                    }

                                } else {
                                    printErroSyn(";", palvraF.get(pLinha + 1).getSimbolo());
                                    listaComandos = false;
                                }

                            } else {
                                printErroSyn(";", palvraF.get(pLinha).getSimbolo());
                                listaComandos = false;
                            }

                        } else {
                            printErroSyn("begin", palvraF.get(pLinha).getSimbolo());
                            listaComandos = false;
                        }

                    } else {
                        printErroSyn("then", palvraF.get(pLinha).getSimbolo());
                        listaComandos = false;
                    }

                } else {
                    printErroSyn("(", palvraF.get(pLinha).getSimbolo());
                    listaComandos = false;
                }

            } else if (palvraF.get(pLinha + 1).getSimbolo().equalsIgnoreCase("while")) {
                ++pLinha;
                if (palvraF.get(++pLinha).getSimbolo().equalsIgnoreCase("(")) {

                    beginExpressao();

                    if (!palvraF.get(++pLinha).getSimbolo().equalsIgnoreCase(")")) {
                        printErroSyn(")", palvraF.get(pLinha).getSimbolo());
                        listaComandos = false;
                    }

                    if (palvraF.get(++pLinha).getSimbolo().equalsIgnoreCase("do")) {

                        if (palvraF.get(pLinha + 1).getSimbolo().equalsIgnoreCase("begin")) {

                            listaComandos = begin();

                            if (palvraF.get(++pLinha).getSimbolo().equalsIgnoreCase(";")) {
                                listaComandos = true;

                            } else if (palvraF.get(pLinha).getSimbolo().equalsIgnoreCase("else")) {

                                if (palvraF.get(++pLinha).getSimbolo().equalsIgnoreCase(";")) {
                                    listaComandos = true;

                                } else if (palvraF.get(pLinha).getSimbolo().equalsIgnoreCase("begin")) {

                                    pLinha--;

                                    listaComandos = begin();

                                    if (palvraF.get(++pLinha).getSimbolo().equalsIgnoreCase(";")) {
                                        listaComandos = true;

                                    } else {
                                        printErroSyn(";", palvraF.get(pLinha).getSimbolo());
                                        listaComandos = false;
                                    }

                                } else {
                                    printErroSyn(";", palvraF.get(pLinha + 1).getSimbolo());
                                    listaComandos = false;
                                }

                            } else {
                                printErroSyn(";", palvraF.get(pLinha).getSimbolo());
                                listaComandos = false;
                            }

                        } else {
                            printErroSyn("begin", palvraF.get(pLinha).getSimbolo());
                            listaComandos = false;
                        }

                    } else {
                        printErroSyn("do", palvraF.get(pLinha).getSimbolo());
                        listaComandos = false;
                    }

                } else {
                    printErroSyn("(", palvraF.get(pLinha).getSimbolo());
                    listaComandos = false;
                }

            }

            if (palvraF.get(pLinha + 1).getSimbolo().equalsIgnoreCase("end")) {
                lista_de_comandos = false;
                listaComandos = true;
            }
        }
        return listaComandos;
    }

    public boolean varProcedureBeginArguments() {
        boolean varTrust = false;
        if (varValida.matcher(palvraF.get(++pLinha).getSimbolo()).matches()
                && !palvraF.get(pLinha).getClassificacao().equalsIgnoreCase(PALAVRA_RESERVADA)) {
            boolean esperaFechar = true;
            while (esperaFechar) {
                if (palvraF.get(++pLinha).getSimbolo().equalsIgnoreCase(")")) {
                    varTrust = true;
                    esperaFechar = false;
                    break;
                } else if (palvraF.get(pLinha).getSimbolo().equalsIgnoreCase(",")) {
                    if (varValida.matcher(palvraF.get(++pLinha).getSimbolo()).matches()
                            && !palvraF.get(pLinha).getClassificacao().equalsIgnoreCase(PALAVRA_RESERVADA)) {

                    } else {
                        printErroSyn("identificador", palvraF.get(pLinha).getSimbolo());
                        varTrust = false;
                        break;
                    }

                } else {
                    printErroSyn(":", palvraF.get(pLinha).getSimbolo());
                    varTrust = false;
                    break;

                }

            }

        } else {
            printErroSyn("identificador", palvraF.get(pLinha).getSimbolo());
            varTrust = false;
        }
        return varTrust;
    }

    public String beginExpressao() {
        String varTrust = "";
        if (varValida.matcher(palvraF.get(++pLinha).getSimbolo()).matches()
                || realValida.matcher(palvraF.get(pLinha).getSimbolo()).matches()
                || natValida.matcher(palvraF.get(pLinha).getSimbolo()).matches()
                && !palvraF.get(pLinha).getClassificacao().equalsIgnoreCase(PALAVRA_RESERVADA)) {

            boolean esperaAlgumacoisa = true;
            while (esperaAlgumacoisa) {

                if (palvraF.get(++pLinha).getSimbolo().equalsIgnoreCase(";") || palvraF.get(pLinha).getSimbolo().equalsIgnoreCase(")")) {
                    --pLinha;
                    esperaAlgumacoisa = false;
                    break;
                } else if (Arrays.asList(opr_add).contains(palvraF.get(pLinha).getSimbolo())
                        || Arrays.asList(opr_relacionais).contains(palvraF.get(pLinha).getSimbolo())
                        || Arrays.asList(opr_mult).contains(palvraF.get(pLinha).getSimbolo())) {

                    if (varValida.matcher(palvraF.get(++pLinha).getSimbolo()).matches()
                            || realValida.matcher(palvraF.get(pLinha).getSimbolo()).matches()
                            || natValida.matcher(palvraF.get(pLinha).getSimbolo()).matches()
                            && !palvraF.get(pLinha).getClassificacao().equalsIgnoreCase(PALAVRA_RESERVADA)) {

                    } else {
                        printErroSyn("identificador ou um tipo", palvraF.get(pLinha).getSimbolo());
                    }

                } else {
                    printErroSyn(";", palvraF.get(pLinha).getSimbolo());
                }

            }

        } else {
            printErroSyn("identificador ou um tipo", palvraF.get(pLinha).getSimbolo());
        }
        return varTrust;
    }

    public void print(String print) {
        try {
            System.out.println(print);
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void printErroSyn(String print1, String print2) {
        System.err.println("Fatal: erro de sintaxe, esperado um (" + print1 + ")  mas (" + print2 + ") encontrado | Linha: " + palvraF.get(pLinha).getLinha());
        System.exit(0);

    }

    public void printErroSm(String print1, String print2) {
        System.err.println("Fatal: erro de semantica, esperado um (" + print1 + ")  mas (" + print2 + ") encontrado");
        System.exit(0);

    }

}
