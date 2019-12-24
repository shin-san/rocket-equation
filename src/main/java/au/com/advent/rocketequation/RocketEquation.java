package au.com.advent.rocketequation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RocketEquation {

	private static final Logger LOGGER = LoggerFactory.getLogger(RocketEquation.class);

	private static List<Integer> moduleList = new ArrayList<Integer>();

	private static int totalFuel = 0;
	private static int entireFuel = 0;
	private static int totalFuelForModule = 0;

	public static void main(String[] args) {

		getModules();

		LOGGER.info("Total fuel of all modules: {}\n", calculateTotalFuelModule());

		totalFuelForModule = 0;
		LOGGER.info("Total fuel of modules including total fuel requirement: {}", calculateEntireFuel());
	}

	private static int measureFuel(int mass) {
		return (mass/3) - 2;
	}

	private static void getModules() {

		final File modules = new File("src/main/resources/module-list.txt");

		try (BufferedReader br = new BufferedReader(new FileReader(modules))) {

			while (br.ready()) {
				moduleList.add(Integer.parseInt(br.readLine()));
			}
		} catch (Exception ex) {
			LOGGER.error("Exception occurred: {}", ex);
			throw new RuntimeException("Exception occurred");
		}
	}

	private static int calculateTotalFuelModule() {

		Iterator<Integer> iterator = moduleList.iterator();

		while(iterator.hasNext()) {
			int mass = iterator.next();
			totalFuel += measureFuel(mass);
			LOGGER.debug("Total fuel requirement for the module ({}) : {}", mass, totalFuel);
		}

		return totalFuel;
	}

	private static int calculateEntireFuel() {

		Iterator<Integer> iterator = moduleList.iterator();

		while(iterator.hasNext()) {
			totalFuelForModule = 0;
			int mass = iterator.next();
			int totalFuelRequirement = calculateTotalFuelRequirement(mass);
			entireFuel += totalFuelRequirement;
			LOGGER.debug("Total fuel requirement for the module ({}) : {}", mass, totalFuelRequirement);
		}

		return entireFuel;
	}

	private static int calculateTotalFuelRequirement(int nextTotalModuleFuel) {

		int moduleFuel = measureFuel(nextTotalModuleFuel);

		if (moduleFuel <= 0) {
			return totalFuelForModule;
		}

		totalFuelForModule += moduleFuel;
//		LOGGER.info("Fuel for the module ({}) : {}", nextTotalModuleFuel, measureFuel(nextTotalModuleFuel));

		return (calculateTotalFuelRequirement(moduleFuel));
	}

}
