package com.acme.sql.parse.antlr4;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.atn.ParserATNSimulator;
import org.antlr.v4.runtime.atn.PredictionMode;
import org.antlr.v4.runtime.tree.ParseTree;

/**
 * todo
 *
 * @author natimercy
 * @version 1.0.0
 * @since 2023-05-22
 */
public class MysqSqlParseDemo {

    public static final String sql =
            "INSERT INTO t2 (b, c)\n" + "\tVALUES ((SELECT a FROM t1 WHERE b='Chip'), 'shoulder'),\n"
                    + "\t((SELECT a FROM t1 WHERE b='Chip'), 'old block'),\n"
                    + "\t((SELECT a FROM t1 WHERE b='John'), 'toilet'),\n"
                    + "\t((SELECT a FROM t1 WHERE b='John'), 'long,silver'),\n"
                    + "\t((SELECT a FROM t1 WHERE b='John'), 'li''l');";

    public static void main(String[] args) {
        parse(sql);
    }

    public static void parse(String sql) {
        MySqlLexer lexer = new MySqlLexer(CharStreams.fromString(sql));
        TokenStream tokenStream = new CommonTokenStream(lexer);
        MySqlParser parser = new MySqlParser(tokenStream);
        ParserATNSimulator interpreter = parser.getInterpreter();
        MySqlParser.InsertStatementContext insertStatementContext = parser.insertStatement();

        interpreter.setPredictionMode(PredictionMode.SLL);
        for (ParseTree child : insertStatementContext.children) {
            System.out.println(child.getText());
        }

        MySqlParserBaseVisitor visitor = new MySqlParserBaseVisitor<>();
        Object object = visitor.visitInsertStatement(insertStatementContext);
        // System.out.println(object);
    }
}
