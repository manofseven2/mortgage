package nl.ing.mortgage.assessment.demo;

import com.tngtech.archunit.core.importer.ImportOption.DoNotIncludeTests;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.base.DescribedPredicate.alwaysTrue;
import static com.tngtech.archunit.core.domain.JavaClass.Predicates.belongToAnyOf;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

@AnalyzeClasses(packagesOf = DemoApplication.class, importOptions = DoNotIncludeTests.class)
class StructureTest {

    @ArchTest
    static final ArchRule respectsTechnicalArchitectureLayers = layeredArchitecture()
        .consideringAllDependencies()
        .layer("Config").definedBy("..config..")
        .layer("Web").definedBy("..web..")
        .optionalLayer("Service").definedBy("..service..")
        .optionalLayer("Persistence").definedBy("..repository..")
        .layer("Domain").definedBy("..domain..")
        .layer("Util").definedBy("..util..")
        .layer("Exception").definedBy("..exception..")

        .whereLayer("Config").mayNotBeAccessedByAnyLayer()
        .whereLayer("Web").mayOnlyBeAccessedByLayers("Config")
        .whereLayer("Service").mayOnlyBeAccessedByLayers("Web", "Config")
        .whereLayer("Persistence").mayOnlyBeAccessedByLayers("Service", "Web", "Config")
        .whereLayer("Domain").mayOnlyBeAccessedByLayers("Persistence", "Service", "Web", "Config")
        .whereLayer("Util").mayOnlyBeAccessedByLayers("Persistence", "Service", "Web", "Config", "Domain")
        .whereLayer("Exception").mayOnlyBeAccessedByLayers("Persistence", "Service", "Web", "Config", "Domain")

        .ignoreDependency(belongToAnyOf(DemoApplication.class), alwaysTrue());
}
