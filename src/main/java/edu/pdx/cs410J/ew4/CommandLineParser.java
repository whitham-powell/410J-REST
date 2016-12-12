package edu.pdx.cs410J.ew4;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * This handle the parsing of the command line and manages them information gathered in
 * <code>ArrayList</code> collections.
 *
 * @author Elijah Whitham-Powell
 */


public class CommandLineParser {

  private ArrayList<String> toParse;
  private ArrayList<String> providedOptions;
  private ArrayList<String> providedArgs;
  private ArrayList<String> claimedArgs;
  private ArrayList<String> invalidOptions;
  private Options validOptions;
  private int expectedNumOfArgs = 0;
  private int maxNumberOfOptions;

  /**
   * Instantiates a new Command line parser.
   *
   * @param validOptions the valid options
   * @param args         the args
   */
  public CommandLineParser(Options validOptions, String[] args) {
    this.validOptions = validOptions;
    toParse = new ArrayList<>();
    Collections.addAll(this.toParse, args);
    this.providedOptions = new ArrayList<>();
    this.providedArgs = new ArrayList<>();
    this.claimedArgs = new ArrayList<>();
    this.invalidOptions = new ArrayList<>();
  }

  /**
   * Does the actual work of parsing the command line. This method uses its parameters to decide where in the command
   * line, the options and arguments should be located.
   *
   * @param numOfArgs controls the loop to find options in the command line.
   * @return {@link Commands} object either containing a collection of commands or the error that occurred while parsing.
   */
  public Commands parse(int numOfArgs) {

    Commands commands = new Commands(false, "No error detected while parsing argument list.");
    int i = 0, toParseSize = toParse.size();
    int expectedOptionStopIndex = toParseSize - numOfArgs;
    this.maxNumberOfOptions = validOptions.getList().size();
    this.expectedNumOfArgs = numOfArgs;


    if (toParse.isEmpty() || expectedOptionStopIndex < 0) {
      if (!toParse.contains("-README")) {
        return new Commands(true, "Missing command line arguments.");
      } else {
        Commands theCommands = new Commands(false, "-README found in options");
        theCommands.add(new Commands.Command("README"));
        return theCommands;
      }
    }

    for (; i < expectedOptionStopIndex; i++) {

      String arg = toParse.get(i);

      providedOptions.add(arg.substring(1));
      // if arg is a valid option store into Commands
      Options.Option option = validOptions.getOption(arg.substring(1));
      if (option != null) {
        if (option.hasArgs()) {
          commands.add(new Commands.Command(option.getName(), option.hasArgs(), toParse.get(i + 1)));
          ++i;
        } else {
          commands.add(new Commands.Command(option.getName(), option.hasArgs()));
        }
      } else {
        invalidOptions.add(arg);
      }
    }

    // Assume all remaining toParse are arguments to use in main
    providedArgs.addAll(toParse.subList(expectedOptionStopIndex, toParseSize));

    if (!invalidOptions.isEmpty()) {
      return new Commands(true, "Invalid option detected: " + invalidOptions);
    }

    return commands;
  }

  /**
   * Helper method to traverse the providedOptions looking for invalid options.
   *
   * @return boolean of found invalid options
   */
  private boolean findInvalidOptions() {
    invalidOptions.addAll(providedOptions
            .stream()
            .filter(option -> !validOptions.getList().contains(option))
            .collect(Collectors.toList()));
    return !invalidOptions.isEmpty();
  }

  /**
   * Builds a string indicating what went wrong while parsing the command line
   *
   * @return String error message
   */
  private String errOut() {
    StringBuffer errMsg = new StringBuffer("Detected:");
    errMsg.append("\n\tDetected Options:");
/*    providedOptions.forEach(option -> errMsg
            .append("\n\t\t")
            .append(option)
            .append(invalidOptions.contains(option) ? " <-Invalid" : ""));*/

    errMsg.append("\n\tArguments Expected: ").append(this.expectedNumOfArgs)
            .append(" Found: ").append(providedArgs.size());

    providedArgs
            .forEach(arg -> errMsg
                    .append("\n\t\t").append(arg));

    errMsg.append("\n\tFrom parsed command line:");
    toParse
            .forEach(parsed -> errMsg
                    .append("\n\t\t")
                    .append(parsed));

    errMsg.append("\n");
    return errMsg.toString();
  }

  /**
   * Gets provided args.
   *
   * @return the provided args
   */
  public ArrayList<String> getProvidedArgs() {
    return providedArgs;
  }

  /**
   * Gets provided options.
   *
   * @return the provided options
   */
  public ArrayList<String> getProvidedOptions() {
    return providedOptions;
  }

  /**
   * Gets to parse.
   *
   * @return the to parse
   */
  public ArrayList<String> getToParse() {
    return toParse;
  }

  /**
   * Gets claimed args.
   *
   * @return the claimed args
   */
  public ArrayList<String> getClaimedArgs() {
    return claimedArgs;
  }


  /**
   * Manages a collection of options to look for while parsing the command line.
   *
   * @author Elijah Whitham-Powell
   */
  static class Options {
    private ArrayList<Option> validOptions;
    private int numWArgs = 0;
    private int numWOArgs = 0;

    /**
     * Instantiates a new Options.
     */
    public Options() {
      validOptions = new ArrayList<>();
    }


    /**
     * Instantiates a new Options.
     *
     * @param options the options
     */
    public Options(Option... options) {
      this.validOptions = new ArrayList<>();
      for (Option o : options) {
        if (o.hasArgs()) {
          ++numWArgs;
        } else {
          ++numWOArgs;
        }
        this.validOptions.add(o);
      }
    }

    /**
     * Gets list.
     *
     * @return the list
     */
    public ArrayList<String> getList() {
      ArrayList<String> listOfOptionsString = new ArrayList<>();
      if (this.validOptions.isEmpty()) {
        return listOfOptionsString;
      } else {
        listOfOptionsString.addAll(this.validOptions.stream().map(Option::getName).collect(Collectors.toList()));
        return listOfOptionsString;
      }
    }

    /**
     * Count int.
     *
     * @return the int
     */
    public int count() {
      assert (numWArgs + numWOArgs == validOptions.size());
      return this.validOptions.size();
    }

    /**
     * Add option boolean.
     *
     * @param optionName  the option
     * @param hasArg      the has argName
     * @param arg         the arg
     * @param description the description  @return the boolean
     * @return the boolean
     */
    public boolean addOption(String optionName, boolean hasArg, String arg, String description) {
      if (validOptions.add(new Option(optionName, hasArg, arg, description))) {
        if (hasArg) {
          ++numWArgs;
        } else {
          ++numWOArgs;
        }
        return true;
      }
      return false;
    }

    /**
     * Add option boolean.
     *
     * @param optionName  the option name
     * @param hasArg      the has arg
     * @param description the description
     * @return the boolean
     */
    public boolean addOption(String optionName, boolean hasArg, String description) {
      if (validOptions.add(new Option(optionName, hasArg, description))) {
        if (hasArg) {
          ++numWArgs;
        } else {
          ++numWOArgs;
        }
        return true;
      }
      return false;
    }

    /**
     * Add option boolean.
     *
     * @param option the option to be added.
     * @return the boolean
     */
    public boolean addOption(Option option) {
      if (validOptions.add(option)) {
        if (option.hasArgs()) {
          ++numWArgs;
        } else {
          ++numWOArgs;
        }
        return true;
      }
      return false;
    }

    /**
     * Gets option.
     *
     * @param s the option name to get.
     * @return the option
     */
    public Option getOption(String s) {
      int index = validOptions.indexOf(new Option(s));
      if (index != -1)
        return validOptions.get(index);
      else
        return null;
    }

    @Override
    public String toString() {
      if (validOptions.isEmpty()) {
        return "Options list is empty.\n";
      }
      StringBuffer sb = new StringBuffer(" options are (may appear in any order):\n");
      for (Option option : validOptions) {
        sb.append("   ").append(option.toString()).append("\n");
      }
      return sb.toString();
    }

    /**
     * Number of options with arguments.
     *
     * @return the int
     */
    public int numWArgs() {
      return this.numWArgs;
    }

    /**
     * Number of options without arguments.
     *
     * @return the int
     */
    public int numWOArgs() {
      return this.numWOArgs;
    }

    /**
     * Inner class of <code>Options</code> manages the information for a single <code>Option</code>.
     * Each option has a name, boolean of whether it takes an argument or not and a description.
     *
     * @author Elijah Whitham-Powell
     */
    public static class Option {
      private String name;
      private boolean hasArgs;
      private String description;
      private String argName;

      /**
       * Instantiates a new Option.
       */
      public Option() {
        this.name = null;
        this.hasArgs = false;
        this.description = null;
        this.argName = null;
      }

      /**
       * Instantiates a new Option.
       *
       * @param optionName the option name
       */
      public Option(String optionName) {
        this.name = optionName;
      }

      /**
       * Instantiates a new Option.
       *
       * @param optionName  the option name
       * @param hasArgs     the has args
       * @param description the description
       */
      public Option(String optionName, boolean hasArgs, String description) {
        this.name = optionName;
        this.hasArgs = hasArgs;
        this.description = description;
      }

      /**
       * Instantiates a new Option.
       *
       * @param name        the name
       * @param hasArgs     the has args
       * @param argName     the argument name
       * @param description the description
       */
      public Option(String name, boolean hasArgs, String argName, String description) {
        this.name = name;
        this.hasArgs = hasArgs;
        this.argName = argName;
        this.description = description;
      }

      /**
       * Instantiates a new Option.
       *
       * @param optionName the option name
       * @param hasArgs    the has args
       */
      public Option(String optionName, boolean hasArgs) {
        this.name = optionName;
        this.hasArgs = hasArgs;
      }

      @Override
      public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Option option = (Option) o;
        return Objects.equals(name, option.name);
      }

      @Override
      public int hashCode() {
        return Objects.hash(name);
      }

      @Override
      public String toString() {
        if (name == null || description == null) {
          return null;
        } else {
          StringBuffer sb = new StringBuffer("-");
          sb.append(name);
          if (hasArgs) {
            sb.append(" <").append(this.argName).append(">\t");
          } else {
            sb.append("\t\t\t");
          }
          sb.append("\t");
          sb.append(description);
          return sb.toString();
        }
      }

      /**
       * Has arguments boolean.
       *
       * @return the boolean
       */
      public boolean hasArgs() {
        return this.hasArgs;
      }

      /**
       * Gets name.
       *
       * @return the name
       */
      public String getName() {
        return this.name;
      }

      /**
       * Gets argument name.
       *
       * @return the argName
       */
      public String getArgName() {
        return this.argName;
      }
    }
  }

  /**
   * The <code>Commands</code> class manages commands parsed from the command line and any errors that may have been
   * encountered. Used in conjunction with:
   * {@link CommandLineParser}
   * {@link Options}
   * {@link Command}
   *
   * @author Elijah Whitham-Powell
   */
  public static class Commands {
    private boolean error = false;
    private String errorMessage = "";
    private ArrayList<Command> commands = new ArrayList<>();

    /**
     * Instantiates a new Commands.
     *
     * @param error        the error.
     * @param errorMessage the error message.
     */
    public Commands(boolean error, String errorMessage) {
      this.error = error;
      this.errorMessage = errorMessage;
    }

    /**
     * Instantiates a new Commands.
     *
     * @param commandsToAdd the commands to add
     */
    public Commands(Command... commandsToAdd) {
      Collections.addAll(this.commands, commandsToAdd);
    }

    /**
     * Instantiates a new Commands.
     *
     * @param error         the error
     * @param errorMessage  the error message
     * @param commandsToAdd the commands to add
     */
    public Commands(boolean error, String errorMessage, Command... commandsToAdd) {
      this.error = error;
      this.errorMessage = errorMessage;
      Collections.addAll(this.commands, commandsToAdd);
    }

    /**
     * Has error boolean.
     *
     * @return the boolean
     */
    public boolean hasError() {
      return this.error;
    }

    /**
     * Gets error message.
     *
     * @return the error message
     */
    public String getErrorMessage() {
      return errorMessage;
    }

    /**
     * Has option boolean.
     *
     * @param optionName the option name
     * @return the boolean
     */
    public boolean hasOption(String optionName) {
      return this.commands.contains(new Command(optionName));
    }

    /**
     * Add boolean.
     *
     * @param command the command
     * @return the boolean
     */
    public boolean add(Command command) {
      return this.commands.add(command);
    }

    /**
     * Gets option value.
     *
     * @param optionName the option name
     * @return the option value
     */
    public String getOptionValue(String optionName) {
      int index = this.commands.indexOf(new Command(optionName));
      return this.commands.get(index).getArgument();
    }

    /**
     * Gets commands list string.
     *
     * @return the commands list string
     */
    public String getCommandsListString() {
      StringBuilder sb = new StringBuilder();
      for (Command c :
              commands) {
        sb.append(c.getName()).append(" ").append(c.getArgument()).append("\n");
      }
      return sb.toString();
    }

    /**
     * Gets commands list.
     *
     * @return the commands list
     */
    public ArrayList<Command> getCommandsList() {
      return commands;
    }

    /**
     * Count int.
     *
     * @return the int
     */
    public int count() {
      return commands.size();
    }

    /**
     * Inner class of <code>Commands</code> extends {@link Options.Option}
     *
     * @author Elijah Whitham-Powell
     */
    public static class Command extends Options.Option {
      private String argument;

      /**
       * Instantiates a new Command.
       *
       * @param optionName the option name
       * @param hasArgs    the has args
       * @param argument   the argument
       */
      public Command(String optionName, boolean hasArgs, String argument) {
        super(optionName, hasArgs);
        this.argument = argument;
      }

      /**
       * Instantiates a new Command.
       *
       * @param optionName the option name
       * @param hasArgs    the has args
       */
      public Command(String optionName, boolean hasArgs) {
        super(optionName, hasArgs);
      }

      /**
       * Instantiates a new Command.
       *
       * @param optionName the option name
       * @param argument   the argument
       */
      public Command(String optionName, String argument) {
        super(optionName);
        this.argument = argument;
      }

      /**
       * Instantiates a new Command.
       *
       * @param optionName the option name
       */
      public Command(String optionName) {
        super(optionName);
      }

      /**
       * Instantiates a new Command.
       */
      public Command() {

      }

      /**
       * Gets argument.
       *
       * @return the argument
       */
      public String getArgument() {
        if (hasArgs()) {
          return argument;
        } else {
          return null;
        }
      }

      @Override
      public String toString() {
        return this.getName();
      }
    }
  }
}






