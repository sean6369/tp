# Exact Paths and Settings

## Java JDK Path
C:\Users\xiang\.jdk\jdk-17.0.9

## Gradle Path
C:\Users\xiang\tp\gradlew.bat

## VS Code Settings.json Sections

### Java Configuration
```json
{
    "java.configuration.runtimes": [
        {
            "name": "JavaSE-17",
            "path": "C:\\Users\\xiang\\.jdk\\jdk-17.0.9",
            "default": true
        }
    ],
    "java.jdt.ls.java.home": "C:\\Users\\xiang\\.jdk\\jdk-17.0.9",
    "java.import.gradle.java.home": "C:\\Users\\xiang\\.jdk\\jdk-17.0.9"
}
```

### Gradle Configuration
```json
{
    "java.import.gradle.java.home": "C:\\Users\\xiang\\.jdk\\jdk-17.0.9",
    "gradle.java.home": "C:\\Users\\xiang\\.jdk\\jdk-17.0.9"
}
```

### Additional Java/Gradle Settings
```json
{
    "[java]": {
        "editor.tabSize": 4,
        "editor.insertSpaces": true,
        "editor.formatOnSave": true,
        "editor.defaultFormatter": "redhat.java"
    },
    "java.format.enabled": true,
    "redhat.java.format.enabled": true,
    "java.format.settings.indentSwitchCase": false,
    "redhat.java.format.indentSwitchCase": false,
    "java.references.codeLens.enabled": true,
    "java.implementations.codeLens.enabled": true,
    "java.codeLens.enabled": true,
    "java.saveActions.organizeImports": false,
    "java.codeGeneration.generateComments": true,
    "java.codeGeneration.useBlocks": true,
    "java.codeGeneration.hashCodeEquals.useInstanceof": true,
    "java.codeGeneration.hashCodeEquals.useJava7Objects": true,
    "java.completion.enabled": true,
    "java.completion.guessMethodArguments": "insertBestGuessedArguments",
    "java.configuration.updateBuildConfiguration": "disabled"
}
```