package org.akhil.buyetbuftest;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufHolder;

public class ByteBufHolderTest {
	
	public static void main(String[] args) {
		ByteBufHolder holder = new ByteBufHolder() {
			
			@Override
			public boolean release(int decrement) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean release() {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public int refCnt() {
				// TODO Auto-generated method stub
				return 0;
			}
			
			@Override
			public ByteBufHolder retain(int increment) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public ByteBufHolder retain() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public ByteBufHolder duplicate() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public ByteBufHolder copy() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public ByteBuf content() {
				// TODO Auto-generated method stub
				return null;
			}
		};
	}

}
