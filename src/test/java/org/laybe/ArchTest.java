package org.laybe;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

class ArchTest {

    @Test
    void servicesAndRepositoriesShouldNotDependOnWebLayer() {

        JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("org.laybe");

        noClasses()
            .that()
                .resideInAnyPackage("org.laybe.service..")
            .or()
                .resideInAnyPackage("org.laybe.repository..")
            .should().dependOnClassesThat()
                .resideInAnyPackage("..org.laybe.web..")
        .because("Services and repositories should not depend on web layer")
        .check(importedClasses);
    }
}
