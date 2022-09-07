package com.ceiba.devfest;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.library.Architectures;
import org.junit.jupiter.api.Test;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

class ApplicationTests {

	JavaClasses javaClasses = new ClassFileImporter()
			.withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
			.withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_ARCHIVES)
			.withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_JARS)
			.importPackages("com.ceiba.devfest");

	@Test
	void controllerDependsOnlyService() {

		classes()
				.that().resideInAPackage("..controller..")
				.should().onlyDependOnClassesThat()
				.resideInAnyPackage("..service..", "java..","org.springframework..")
				.check(javaClasses);
	}

	@Test
	void domainDependsOnlyJava() {

		classes()
				.that().resideInAPackage("..domain..")
				.should().onlyDependOnClassesThat()
				.resideInAnyPackage("java..")
				.check(javaClasses);
	}

	@Test
	void layersCheck() {

		Architectures.layeredArchitecture()
				// Define layers
				.layer("Controller").definedBy("..controller..")
				.layer("Service").definedBy("..service..")
				.layer("Persistence").definedBy("..persistence..")
				// Add constraints
				.whereLayer("Controller").mayNotBeAccessedByAnyLayer()
				.whereLayer("Service").mayOnlyBeAccessedByLayers("Controller")
				.whereLayer("Persistence").mayOnlyBeAccessedByLayers("Service")
				.check(javaClasses);
	}

	@Test
	void persistenceClassesShouldImplementSerializable() {

		classes()
				.that().resideInAPackage("..persistence..")
				.should().implement(Serializable.class).check(javaClasses);
	}

	@Test
	void controllersWithSufixAndAnnotation() {

		classes()
				.that().resideInAPackage("..controller..")
				.should().haveSimpleNameEndingWith("Controller")
				.andShould().beAnnotatedWith(RestController.class)
				.andShould().bePublic().check(javaClasses);
	}

	@Test
	void contractShouldBeOnlyInterface() {

		classes()
				.that().resideInAPackage("..service.contract..")
				.should().beInterfaces().check(javaClasses);
	}
}
