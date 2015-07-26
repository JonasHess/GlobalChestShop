package eu.blockup.GlobalChestShop.Util.Statements;

public class VipGroup {
	
	private int worldGroup;
	private String permissionGroup;
	private int exceedingLimit;
	
	
	public VipGroup(int worldGroup, String permissionGroup, int exceedingLimit) {
		super();
		this.worldGroup = worldGroup;
		this.permissionGroup = permissionGroup;
		this.exceedingLimit = exceedingLimit;
	}


	public int getWorldGroup() {
		return worldGroup;
	}


	public String getPermissionGroup() {
		return permissionGroup;
	}


	public int getExceedingLimit() {
		return exceedingLimit;
	}
	
	
	

}
