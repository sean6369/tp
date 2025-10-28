# Java Coding Standards and Git Conventions

This document outlines the coding standards for Java and Git conventions to follow in this project. It draws from SE-EDU guides and best practices to ensure consistency, readability, and maintainability. As part of the Developer Guide (DG), remember to showcase your documentation skills by explaining designs clearly, using diagrams, and citing examples appropriately.

## Developer Guide Tips

Aim to showcase your documentation skills. The primary objective of the DG is to explain the design/implementation to a future developer, but a secondary objective is to serve as evidence of your ability to document deeply-technical content using prose, examples, diagrams, code snippets, etc., appropriately. To that end, you may also describe features that you plan to implement in the future, even beyond v2.1 (hypothetically).

For an example, see the description of the undo/redo feature implementation in the AddressBook-Level3 developer guide.

### Use Multiple UML Diagram Types

Following from the point above, try to include UML diagrams of multiple types to showcase your ability to use different UML diagrams.

#### FAQ: What do we do when the UML notation covered in the course is not enough to model what we want to illustrate?

Here are some options you can consider:

- Use the closest matching notation, and use UML notes to provide the missing information.
- Use an alternative means to communicate (e.g., pseudocode, or even actual code), instead of using a UML diagram.
- Leave out the complex part from the diagram, or give a simpler view in the model, if the complex part is not really relevant to the purpose of the diagram (but also mention that the model is a simplified view).
- Caution: For course deliverables, it is best not to use UML notations not covered in the course.

#### Diagramming Tools

AB3 uses PlantUML (see the guide [Using PlantUML @SE-EDU/guides](https://se-education.org/guides/tutorials/plantUml.html) for more info).

##### FAQ: Instead of PlantUML, can I use some other tool?

Yes, you may use any other tool too (e.g., PowerPoint). But wait; if you do, note the following:

- Choose a diagramming tool that has some 'source' format that can be version-controlled using git and updated incrementally (reason: because diagrams need to evolve with the code that is already being version controlled using git). For example, if you use PowerPoint to draw diagrams, also commit the source PowerPoint files so that they can be reused when updating diagrams later.
- Use the same diagramming tool for the whole project, except in cases for which there is a strong need to use a different tool due to a shortcoming in the primary diagramming tool. Do not use a mix of different tools simply based on personal preferences.
- So far, PlantUML seems to be the best fit for the above requirements.

##### FAQ: Can IDE-generated UML diagrams be used in project submissions?

Not a good idea. Given below are three reasons each of which can be reported by evaluators as 'bugs' in your diagrams, costing you marks:

- They often don't follow the standard UML notation (e.g., they add extra icons).
- They tend to include every little detail whereas we want to limit UML diagrams to important details only, to improve readability.
- Diagrams reverse-engineered by an IDE might not represent the actual design as some design concepts cannot be deterministically identified from the code e.g., differentiating between multiplicities 0..1 vs 1, composition vs aggregation.

### Keep Diagrams Simple

The aim is to make diagrams comprehensible, not necessarily comprehensive.

Ways to simplify diagrams:

- Omit less important details. Examples:
  - A class diagram can omit minor utility classes, private/unimportant members; some less-important associations can be shown as attributes instead.
  - A sequence diagram can omit less important interactions, self-calls, method parameters, etc.
    - You can use `...` (e.g., `foo(...)`) to indicate parameters have been omitted.
    - You can use pseudocode instead of exact method calls e.g., `save data in file` instead of `saveData(content, filename)`.
- Omit intricate details that complicated the diagram unnecessarily they add to the diagram e.g., exception handling (throw/catch), lambdas, calls to anonymous methods, etc.
- If you feel they are important to the purpose of the diagram (i.e., omitting them can mislead the reader), you can use a UML note to mention that information (as plain text) in the diagram.
- Omit repetitive details e.g., a class diagram can show only a few representative ones in place of many similar classes (note how the AB3 Logic class diagram shows concrete `*Command` classes using a placeholder `XYZCommand`).
- Limit the scope of a diagram. Decide the purpose of the diagram (i.e., what does it help to explain?) and omit details not related to it.
- Break diagrams into smaller fragments when possible.
  - If a component has a lot of classes, consider further dividing into subcomponents (e.g., a Parser subcomponent inside the Logic component). After that, subcomponents can be shown as black-boxes in the main diagram and their details can be shown as separate diagrams.
  - You can use ref frames to break sequence diagrams to multiple diagrams.
- Use visual representations as much as possible. E.g., show associations and navigabilities using lines and arrows connecting classes, rather than adding a variable in one of the classes.
- For some more examples of what NOT to do, see [here](https://se-education.org/guides/tutorials/plantUml.html).

### Integrate Diagrams into the Description

- Place the diagram close to where it is being described.
- Use code snippets sparingly. The more you use code snippets in the DG, and longer the code snippet, the higher the risk of it getting outdated quickly. Instead, use code snippets only when necessary and cite only the strictly relevant parts only. You can also use pseudocode instead of actual programming code.
- Resize diagrams so that the text size in the diagram matches the text size of the main text of the diagram. See example.

## Java Coding Standards

Use the [Google Java style guide](https://google.github.io/styleguide/javaguide.html) for any topics not covered in this document.

### Naming

- Names representing packages should be in all lower case.

  ```
  com.company.application.ui
  ```

  More on package naming: For school projects, the root name of the package should be your group name or project name followed by logical group names. e.g. `todobuddy.ui`, `todobuddy.file` etc.
  Rationale: Your code is not officially 'produced by NUS', therefore do not use `edu.nus.comp.*` or anything similar.
- Class/enum names must be nouns and written in PascalCase.

  ```
  Line, AudioSystem
  ```
- Variable names must be in camelCase.

  ```
  line, audioSystem
  ```
- Constant names must be all uppercase using underscore to separate words (aka SCREAMING_SNAKE_CASE). To find what exactly are considered constants, refer to [this page](https://google.github.io/styleguide/javaguide.html#s5.2.4-constant-names) in the Google Java Style Guide.

  ```
  MAX_ITERATIONS, COLOR_RED
  ```
- Names representing methods must be verbs and written in camelCase.

  ```
  getName(), computeTotalWidth()
  ```
- Underscores may be used in test method names using the following three part format `featureUnderTest_testScenario_expectedBehavior()`.
  e.g. `sortList_emptyList_exceptionThrown()` `getMember_memberNotFound_nullReturned`
  Third part or both second and third parts can be omitted depending on what's covered in the test. For example, the test method `sortList_emptyList()` will test `sortList()` method for all variations of the 'empty list' scenario and the test method `sortList()` will test the `sortList()` method for all scenarios.
- All names should be written in English.
  Rationale: The code is meant for an international audience.
- Boolean variables/methods should be named to sound like booleans

  ```
  // variables
  isSet, isVisible, isFinished, isFound, isOpen, hasData, wasOpen

  // methods
  boolean hasLicense();
  boolean canEvaluate();
  boolean shouldAbort = false;
  ```

  As much as possible, use a prefix such as `is`, `has`, `was`, etc. for boolean variable/method names so that linters can automatically verify that this style rule is being followed.
- Setter methods for boolean variables must be of the form:

  ```
  void setFound(boolean isFound);
  ```

  Rationale: This is the naming convention for boolean methods and variables used by Java core packages. It also makes the code read like normal English e.g. `if(isOpen) ...`
- Plural form should be used on names representing a collection of objects.

  ```
  Collection<Point> points;
  int[] values;
  ```

  Rationale: Enhances readability since the name gives the user an immediate clue of the type of the variable and the operations that can be performed on its elements. One space character after the variable type is enough to obtain clarity.
- Iterator variables can be called `i`, `j`, `k` etc.
  Variables named `j`, `k` etc. should be used for nested loops only.

  ```
  for (Iterator i = points.iterator(); i.hasNext(); ) {
      ...
  }

  for (int i = 0; i < nTables; i++) {
      ...
  }
  ```

  Rationale: The notation is taken from mathematics where it is an established convention for indicating iterators.

### Layout

- Basic indentation should be 4 spaces (not tabs).

  ```
  for (i = 0; i < nElements; i++) {
      a[i] = 0;
  }
  ```

  Rationale: Just follow it.
- Line length should be no longer than 120 chars.
  Try to keep line length shorter than 110 characters (soft limit). But it is OK to exceed the limit slightly (hard limit: 120 chars). If the line exceeds the limit, use line wrapping at appropriate places of the line.
  Indentation for wrapped lines should be 8 spaces (i.e. twice the normal indentation of 4 spaces) more than the parent line.

  ```
  setText("Long line split"
          + "into two parts.");
  if (isReady) {
      setText("Long line split"
              + "into two parts.");
  }
  ```
- Use K&R style brackets (aka Egyptian style).
  Good:

  ```
  while (!done) {
      doSomething();
      done = moreToDo();
  }
  ```

  Bad:

  ```
  while (!done)
  {
      doSomething();
      done = moreToDo();
  }
  ```

  Rationale: Just follow it.
- Method definitions should have the following form:

  ```
  public void someMethod() throws SomeException {
      ...
  }
  ```
- The if-else class of statements should have the following form:

  ```
  if (condition) {
      statements;
  }

  if (condition) {
      statements;
  } else {
      statements;
  }

  if (condition) {
      statements;
  } else if (condition) {
      statements;
  } else {
      statements;
  }
  ```
- The for statement should have the following form:

  ```
  for (initialization; condition; update) {
      statements;
  }
  ```
- The while and the do-while statements should have the following form:

  ```
  while (condition) {
      statements;
  }
  do {
      statements;
  } while (condition);
  ```
- The switch statement should have the following form: Note there is no indentation for case clauses. Configure your IDE to follow this style instead.

  ```
  switch (condition) {
  case ABC:
      statements;
      // Fallthrough
  case DEF:
      statements;
      break;
  case XYZ:
      statements;
      break;
  default:
      statements;
      break;
  }
  ```

  Lambda-style switch statements/expressions can have indented case blocks (as shown below):

  ```
  switch (condition) {
      case ABC -> method("1");
      case DEF -> method("2");
      case XYZ -> method("3");
      default -> method("0");
  }
  int size = switch (condition) {
      case ABC -> 1;
      case DEF -> 2;
      case XYZ -> 3;
      default -> 0;
  }
  ```

  The explicit `// Fallthrough` comment should be included whenever there is a case statement without a break statement.
  Rationale: Leaving out the break is a common error, and it must be made clear that it is intentional when it is not there.
- A try-catch statement should have the following form:

  ```
  try {
      statements;
  } catch (Exception exception) {
      statements;
  }

  try {
      statements;
  } catch (Exception exception) {
      statements;
  } finally {
      statements;
  }
  ```

### Statements

#### Package and Import Statements

- Put every class in a package.
  Every class should be part of some package.
  Rationale: It will help you and other developers easily understand the code base when all the classes have been grouped in packages.
- Imported classes should always be listed explicitly.
  Good:

  ```
  import java.util.List;
  import java.util.ArrayList;
  import java.util.HashSet;
  ```

  Bad:

  ```
  import java.util.*;
  ```

  Rationale: Importing classes explicitly gives an excellent documentation value for the class at hand and makes the class easier to comprehend and maintain. Appropriate tools should be used in order to always keep the import list minimal and up to date. IDE's can be configured to do this easily.

#### Types

- Array specifiers must be attached to the type not the variable.
  Good:

  ```
  int[] a = new int[20];
  ```

  Bad:

  ```
  int a[] = new int[20];
  ```

  Rationale: The arrayness is a feature of the base type, not the variable. Java allows both forms however.

#### Loops

- The loop body should be wrapped by curly brackets irrespective of how many lines there are in the body.
  Good:

  ```
  for (i = 0; i < 100; i++) {
      sum += value[i];
  }
  ```

  Bad:

  ```
  for (i = 0, sum = 0; i < 100; i++)
      sum += value[i];
  ```

  Rationale: When there is only one statement in the loop body, Java allows it to be written without wrapping it between `{ }`. However that is error prone and very strongly discouraged from using.

#### Conditionals

- The conditional should be put on a separate line.
  Good:

  ```
  if (isDone) {
      doCleanup();
  }
  ```

  Bad:

  ```
  if (isDone) doCleanup();
  ```

  Rationale: This helps when debugging using an IDE debugger. When writing on a single line, it is not apparent whether the condition is really true or not.
- Single statement conditionals should still be wrapped by curly brackets.
  Good:

  ```
  InputStream stream = File.open(fileName, "w");
  if (stream != null) {
      readFile(stream);
  }
  ```

  Bad:

  ```
  InputStream stream = File.open(fileName, "w");
  if (stream != null)
      readFile(stream);
  ```

  The body of the conditional should be wrapped by curly brackets irrespective of how many statements.
  Rationale: Omitting braces can lead to subtle bugs.

### Comments

- All comments should be written in English.
  Choose either American or British spelling and use it consistently.
  Avoid local slang.
  Rationale: The code is meant for an international audience.
- Javadoc comments should have the following form:

  ```
  /**
   * Returns lateral location of the specified position.
   * If the position is unset, NaN is returned.
   *
   * @param x X coordinate of position.
   * @param y Y coordinate of position.
   * @param zone Zone of position.
   * @return Lateral location.
   * @throws IllegalArgumentException If zone is <= 0.
   */
  public double computeLocation(double x, double y, int zone)
          throws IllegalArgumentException {
      // ...
  }
  ```

  Note in particular:

  - The opening `/**` on a separate line.
  - Write the first sentence as a short summary of the method, as Javadoc automatically places it in the method summary table (and index).
  - In method header comments, the first sentence should start in the form `Returns ...`, `Sends ...`, `Adds ...` etc. (not `Return` or `Returning` etc.)
  - Subsequent `*` is aligned with the first one.
  - Space after each `*`.
  - Empty line between description and parameter section.
  - Punctuation behind each parameter description.
  - No blank line between the documentation block and the method/class.
  - `@return` can be omitted if the method does not return anything or the return value is obvious from the rest of the comment.
  - `@params` can be omitted if all parameters of a method have self-explanatory names, or they are already explained in the main part of the comment i.e., if none of the `@params` add any value. This means the comment will have `@param` for all its parameters, or none.
  - When writing Javadocs for overridden methods, the `@inheritDoc` tag can be used to reuse the header comment from the parent method but with further modifications e.g., when the method has a slightly different behavior from the parent method.
  - Javadoc of class members can be specified on a single line as follows:
    ```
    /** Number of connections to this database */
    private int connectionCount;
    ```
- Comments should be indented relative to their position in the code.
  Good:

  ```
  while (true) {
      // Do something
      something();
  }
  ```

  Bad:

  ```
  while (true) {
          // Do Something
      something();
  }
  ```

  Bad:

  ```
  while (true) {
  // Do Something
      something();
  }
  ```

  Rationale: This is to avoid the comments from breaking the logical structure of the program.
  Note that trailing comments such as the below are allowed as well.

  ```
      process('ABC'); // process a dummy String frst
  ```

### References

- [Oracle&#39;s Java Style Guide](https://www.oracle.com/docs/tech/java/codeconventions.pdf)
- [Google&#39;s Java Style Guide](https://google.github.io/styleguide/javaguide.html)

### Contributors

- Nimantha Baranasuriya - Initial draft
- Dai Thanh - Further tweaks
- Tong Chun Kit - Further tweaks
- Barnabas Tan - Converted from Google Docs to Markdown Document

## Git Conventions

Legend: basic rule | intermediate rule | advanced rule

### Commit Message: Subject

- Every commit must have a well-written commit message subject line.
  Try to limit the subject line to 50 characters (hard limit: 72 chars)
  Rationale: Some tools show only a limited number of characters from the commit message.
- Use the imperative mood in the subject line.
  Good: `Add README.md`
  Bad: `Added README.md`
  Bad: `Adding README.md`
- Capitalize the first letter of the subject line.
  Good: `Move index.html file to root`
  Bad: `move index.html file to root`
- Do not end the subject line with a period.
  Good: `Update sample data`
  Bad: `Update sample data.`
- You may add a `<scope>:` or `<category>:` in front, when applicable.
  e.g. `Person class: Remove static imports`
  `Main.java: Remove blank lines`
  `bug fix: Add space after name`
  `chore: Update release date`
  There are other commit subject conventions such as the [Conventional Commits Format](https://conventionalcommits.org/) which are more elaborate but have additional benefits.

### Commit Message: Body

- Commit messages for non-trivial commits should have a body giving details of the commit.
  Separate subject from body with a blank line.
  Wrap the body at 72 characters.
  Use blank lines to separate paragraphs.

  Example: A commit message for a commit that is part of a multi-commit PR:

  ```
  Unify variations of toSet() methods

  There are several methods that convert a collection to a set. In some
  cases the conversion is in-lined as a code block in another method.

  Unifying all those duplicated code improves the code quality.

  As a step towards such unification, let's extract those duplicated code
  blocks into separate methods in their respective classes. Doing so will
  make the subsequent unification easier.
  ```

  Tips for SourceTree users:

  - Enable the column guide option to help you keep the commit message width to a 72 characters:
    Choose Tools -> Options.
    Click on the General tab, scroll down to the Commit settings section.
    Tick the `Display a column guide at commit message at [72] characters` option (if this option is disabled, first tick the option `used fixed-width fonts for commit messages` -- reason: a column guide makes sense only if a fixed-width font is used).
  - Enable the spell check commit messages option (found in the same place as above) too.
- Use bullet points as necessary. Instead of relying entirely on paragraphs of text, use other constructs such as bullet lists when it helps.
  Example: A commit message for a bug fix:

  ```
  Find command: make matching case-insensitive

  Find command is case-sensitive.

  A case-insensitive find is more user-friendly because users cannot be
  expected to remember the exact case of the keywords.

  Let's,
  * update the search algorithm to use case-insensitive matching
  * add a script to migrate stress tests to the new format
  ```
- Explain WHAT, WHY, not HOW.
  Use the body to explain WHAT the commit is about and WHY it was done that way. The reader can refer to the diff to understand HOW the change was done.
  Give an explanation for the change(s) that is detailed enough so that the reader can judge if it is a good thing to do, without reading the actual diff to determine how well the code does what the explanation promises to do.
  If your description starts to get too long, that's a sign that you probably need to split up your commit to finer grained pieces. [adapted from: git project]
  Minimize repeating information that are given in code comments of the same commit.
- Structure the body as follows:

  ```
  {current situation} -- use present tense

  {why it needs to change}

  {what is being done about it} -- use imperative mood

  {why it is done that way}

  {any other relevant info}
  ```

  Avoid terms such as 'currently', 'originally' when describing the current situation. They are implied.
  The word `Let's` can be used to indicate the beginning of the section that describes the change done in the commit.

  Example: A commit message for a code quality refactoring:

  ```
  Person attributes classes: extract a parent class PersonAttribute

  Person attribute classes (e.g. Name, Address, Age etc.) have some common
  behaviors (e.g. isValid()).

  The common behaviors across person attribute classes cause code duplication.

  Extracting the common behavior into a super class allows us to use
  polymorphism when dealing with person attributes. For example, validity
  checking can be done for all attributes of a person in one loop.

  Let's pull up behaviors common to all person attribute classes into a new
  parent class named PersonAttribute.

  Using inheritance is preferable over composition in this situation
  because the common behaviors are not composable.

  Refer to this S/O discussion on dealing with attributes
  http://stackoverflow.com/some/question
  ```

  Refer to the article [How to Write a Git Commit Message](https://chris.beams.io/posts/git-commit/) for more advice on writing good commit messages.

### Branch Names

Follow these rules to improve consistency:

- Use a meaningful name consisting of some relevant keywords, in the kebab case format e.g., `refactor-ui-tests`.
- If the branch is related to an issue, use the format `issueNumber-some-keywords-from-issue-title` e.g., `1234-ui-freeze-error`
