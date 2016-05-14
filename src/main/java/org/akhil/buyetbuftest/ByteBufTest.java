package org.akhil.buyetbuftest;

import java.util.Random;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;

public class ByteBufTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ByteBuf buf = Unpooled.copiedBuffer("Akhil rocks!", CharsetUtil.UTF_8);
		if(buf.hasArray()) {
			System.out.println("Heap Bufer");
		} else {
			System.out.println("Direct Buffer");
		}
		/*for(int i = 0; i < buf.capacity(); i++) {
			byte b = buf.getByte(i);
			System.out.println("Char @ " + i + " : " + (char) b);
		}*/
		System.out.println("REader Index : " + buf.readerIndex() + " ;; Writer Index : " + buf.writerIndex() + ";; capacity : " + buf.capacity() + ";; maxCapacity : " + buf.maxCapacity());
		while(buf.readableBytes() > 0) {
			byte b = buf.readByte();
			System.out.println("Byte : " + (char) b);
		}
		System.out.println("REader Index : " + buf.readerIndex() + " ;; Writer Index : " + buf.writerIndex() + ";; capacity : " + buf.capacity() + ";; maxCapacity : " + buf.maxCapacity());
		Random random = new Random();
		while(buf.writableBytes() > 4) {
			int rand = random.nextInt()%10;
			System.out.println("Rand int : " + rand);
			buf.writeInt(rand);
		}
		System.out.println("REader Index : " + buf.readerIndex() + " ;; Writer Index : " + buf.writerIndex() + ";; capacity : " + buf.capacity() + ";; maxCapacity : " + buf.maxCapacity());
		while(buf.readableBytes() > 0) {
			int b = buf.readInt();
			System.out.println("Int : " + b);
		}
		System.out.println("REader Index : " + buf.readerIndex() + " ;; Writer Index : " + buf.writerIndex() + ";; capacity : " + buf.capacity() + ";; maxCapacity : " + buf.maxCapacity());

	}

}
