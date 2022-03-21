package fr.leflodu62.universe.world.chunk;

public class ChunkPos {
	
	private int x, y, z;
	
	public ChunkPos(ChunkPos other) {
		this(other.getX(), other.getY(), other.getZ());
	}
	
	public ChunkPos(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getZ() {
		return z;
	}

	@Override
	public int hashCode() {
		int result = 1;
		result = result << 5 - result + x;
		result = result << 5 - result + y;
		result = result << 5 - result + z;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ChunkPos other = (ChunkPos) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		if (z != other.z)
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "ChunkPos(" + x + ", " + y + ", " + z + ")";
	}

}
