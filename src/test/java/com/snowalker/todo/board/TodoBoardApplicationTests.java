package com.snowalker.todo.board;

import org.apache.commons.cli.*;
import org.junit.Test;

public class TodoBoardApplicationTests {

    @Test
    public void test1() throws ParseException {
        String[] args = new String[]{"-help"};
        // Create a Parser
        CommandLineParser parser = new DefaultParser( );
        Options options = new Options( );
        options.addOption("h", "help", false, "Print this usage information");
        options.addOption("v", "verbose", false, "Print out VERBOSE information" );
        options.addOption("f", "file", true, "File to save program output to");
        // Parse the program arguments
        CommandLine commandLine = parser.parse( options, args);
        // Set the appropriate variables based on supplied options
        boolean verbose = false;
        String file = "";
        if( commandLine.hasOption('h') ) {
            System.out.println( "Help Message");
            System.exit(0);
        }
        if( commandLine.hasOption('v') ) {
            verbose = true;
        }
        if( commandLine.hasOption('f') ) {
            file = commandLine.getOptionValue('f');
        }
    }

}
