package com.ceiba.devfest;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.library.Architectures;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

class ApplicationTests {

	@Test
	void presentationDependsOnlyService() {

		JavaClasses jc = new ClassFileImporter()
				.importPackages("com.ceiba.devfest");

		ArchRule r1 = classes()
				.that().resideInAPackage("..presentation..")
				.should().onlyDependOnClassesThat()
				.resideInAnyPackage("..service..", "java..");

		r1.check(jc);
	}

	@Test
	void presentationNotDependsOnPersistence() {

		JavaClasses jc = new ClassFileImporter()
				.importPackages("com.ceiba.devfest");

		ArchRule r1 = noClasses()
				.that().resideInAPackage("..presentation..")
				.should().dependOnClassesThat()
				.resideInAPackage("..persistence..");

		r1.check(jc);
	}

	@Test
	void layers() {

		JavaClasses jc = new ClassFileImporter()
				.importPackages("com.ceiba.devfest");

		Architectures.LayeredArchitecture arch = Architectures.layeredArchitecture()
				// Define layers
				.layer("Presentation").definedBy("..presentation..")
				.layer("Service").definedBy("..service..")
				.layer("Persistence").definedBy("..persistence..")
				// Add constraints
				.whereLayer("Presentation").mayNotBeAccessedByAnyLayer()
				.whereLayer("Service").mayOnlyBeAccessedByLayers("Presentation")
				.whereLayer("Persistence").mayOnlyBeAccessedByLayers("Service");
		arch.check(jc);
	}

}
