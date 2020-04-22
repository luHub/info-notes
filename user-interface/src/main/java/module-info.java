module com.heguer.info.userinterface {
	opens ui;
	exports ui.util;
	exports model;
	exports model.ops;
	exports ui;

	requires com.fasterxml.jackson.core;
	requires com.fasterxml.jackson.databind;
	requires transitive com.heguer.info.fileio;
	requires com.heguer.info.user;
	requires javafx.base;
	requires javafx.controls;
	requires javafx.fxml;
	requires javafx.graphics;
	requires javafx.web;
}