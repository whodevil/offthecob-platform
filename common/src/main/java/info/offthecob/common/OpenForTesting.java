package info.offthecob.common;

/**
 * Wire this up to the kotlin all open compiler plugin to enable
 * groovy mocking of kotlin classes in spock. The default behavior in
 * kotlin is that kotlin classes are final in java bytecode, this causes
 * spock framework mocking to fail.
 */
public @interface OpenForTesting {
}
