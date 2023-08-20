package info.offthecob.gradle

import net.ltgt.gradle.nullaway.NullAwayExtension

class NullAwayCustomizationTest extends UnitSpec {
    def setupSpec() {
        setupProject("info.offthecob.Base")
    }

    def "annotations set"() {
        when:
        def nullAway = project.extensions.getByType(NullAwayExtension.class)

        then:
        nullAway.annotatedPackages.get().first() == NullAwayCustomizationKt.NULL_AWAY_ANNOTATED_PACKAGES
    }
}
