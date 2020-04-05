package workbook;

import java.sql.Timestamp;

/**
 *
 * @author mey
 */
public class WorkbookItem {
	Timestamp timeStamp;
	WorkbookItemType itemType;

	public WorkbookItem(final WorkbookItemType itemType, final Timestamp timestamp) {
		this.timeStamp = timestamp;
		this.itemType = itemType;
	}

}