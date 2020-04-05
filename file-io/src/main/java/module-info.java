module com.heguer.info.fileio {
	exports workbook;
	exports workspace;
	exports file;
	exports file.dto;
	exports info;

	requires com.fasterxml.jackson.core;
	requires com.fasterxml.jackson.databind;
	requires java.sql;
}