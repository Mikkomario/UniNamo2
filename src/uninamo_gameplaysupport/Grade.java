package uninamo_gameplaysupport;

/**
 * Ranks represent the different levels of skill / success in the game
 * 
 * @author Mikko Hilpinen
 * @since 24.3.2014
 */
public enum Grade
{
	/**
	 * Student is the lowest grade, it should be given only when the player 
	 * really could have done better
	 */
	STUDENT,
	/**
	 * Engineer is the basic grade expected from a reasonably skilled player
	 */
	ENGINEER,
	/**
	 * Savant is a good grade that can be awarded to skillful players
	 */
	SAVANT,
	/**
	 * Master is the highest grade possible and requires near-excellence
	 */
	MASTER;
	
	
	// METHODS	----------------------------------------------------------
	
	/**
	 * Returns a grade based on the difference between the used component 
	 * costs and 
	 * 
	 * @param differencePercentage The difference between the demo component 
	 * costs and the used component costs in percents
	 * @return A grade acquired with the given percentage
	 */
	public static Grade getGradeFromCostDifference(int differencePercentage)
	{
		if (differencePercentage < 5)
			return MASTER;
		if (differencePercentage < 40)
			return SAVANT;
		if (differencePercentage < 75)
			return ENGINEER;
		
		return STUDENT;
	}
	
	/**
	 * @return The spriteIndex for the grade sprite that represents this grade
	 */
	public int getGradeSpriteIndex()
	{
		switch (this)
		{
			case STUDENT: return 0;
			case ENGINEER: return 1;
			case SAVANT: return 2;
			case MASTER: return 3;
		}
		
		return 0;
	}
}
