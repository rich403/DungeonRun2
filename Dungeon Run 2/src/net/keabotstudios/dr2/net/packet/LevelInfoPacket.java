package net.keabotstudios.dr2.net.packet;

import java.net.InetAddress;

import net.keabotstudios.dr2.game.level.Level;
import net.keabotstudios.dr2.game.level.object.block.Block;
import net.keabotstudios.superserial.BinaryWriter;
import net.keabotstudios.superserial.SSSerialization;
import net.keabotstudios.superserial.SSType.SSDataType;

public class LevelInfoPacket extends GamePacket {

	private Level level;

	public LevelInfoPacket(InetAddress address, int port, Level level) {
		super(PacketType.LEVEL_INFO, address, port);
		this.level = level;
	}

	public LevelInfoPacket(byte[] data, InetAddress address, int port) {
		super(PacketType.CONNECT, address, port);
		int pointer = SSDataType.BYTE.getSize() * (PACKET_HEADER.length + 1);
		
		int width = SSSerialization.readInteger(data, pointer);
		pointer += SSDataType.INTEGER.getSize();
		
		int height = SSSerialization.readInteger(data, pointer);
		pointer += SSDataType.INTEGER.getSize();
		
		int[] blockIds = new int[width * height];
		SSSerialization.readIntegers(data, pointer, blockIds);
		pointer += SSDataType.INTEGER.getSize() * blockIds.length;
		
		int[] tileIds = new int[width * height];
		SSSerialization.readIntegers(data, pointer, tileIds);
		pointer += SSDataType.INTEGER.getSize() * tileIds.length;
		
		Block[] blocks = new Block[blockIds.length];
		for (int i = 0; i < blocks.length; i++)
			blocks[i] = Block.blocks[blockIds[i]];
		level = new Level(width, height, blocks);
	}

	public byte[] getData() {
		BinaryWriter data = new BinaryWriter();
		data.write(PACKET_HEADER);
		data.write(type.getId());
		data.write(level.getHeight());
		data.write(level.getWidth());
		for (int i = 0; i < level.getWidth() * level.getHeight(); i++) {
			data.write(level.getBlock(i % level.getWidth(), i / level.getWidth()).getId());
		}
		for (int i = 0; i < level.getWidth() * level.getHeight(); i++) {
			data.write(level.getTile(i % level.getWidth(), i / level.getWidth()).getId());
		}
		return data.getBuffer();
	}

}
