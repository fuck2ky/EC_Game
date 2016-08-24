package io.element.card;

import java.util.Vector;

import com.google.protobuf.ByteString;

import io.element.player.BasePlayer;
import io.element.protobuf.GlobalProto;
import io.element.protobuf.SimpleProto;
import io.element.room.impl.GM_BaseRoom;
import io.element.state.State.CAMP_TYPE;
import io.element.state.State.CHOOSE_TYPE;
import io.element.state.State.InteractiveInfo;
import io.element.state.State.InteractiveInfo.PidValues;
import io.element.state.State.PerformInfo;
import io.element.task.impl.Task_StateRun;
import io.element.util.HandlerUtil;

public class ChooseStrategy implements Strategy {

	// 需要设定默认值
	// 发送请求
	// current state 保存所需的数据
	public int doOperation(PerformInfo info, GM_BaseRoom room, int index) {
		
		if( index >= info.recvs.size() || info.pid == 0 || room == null )
			return 1;
		
		InteractiveInfo iInfo = info.recvs.get(index);
		if(	iInfo.einfo.chooseType == CHOOSE_TYPE.NONE || 
			iInfo.einfo.campType == CAMP_TYPE.CAMP_ALLPLAYER ||
			iInfo.einfo.campType == CAMP_TYPE.CAMP_ENEMY_A||
			iInfo.einfo.campType == CAMP_TYPE.CAMP_FIREND_A || iInfo.performed() )
			return 2;
		
		Vector<Integer> locations = room.getEntityManager()
										.getVaildLocation(iInfo.einfo.chooseType, iInfo.einfo.campType , info.pid);
		if(locations == null)
			return 3;
		
		SimpleProto.S2G_ChoosePlayers.Builder builder = SimpleProto.S2G_ChoosePlayers.newBuilder();
		SimpleProto.S2G_ChoosePlayers msg = builder.setRoomid( room.ID() )
												   .setPlayerid( info.pid )
												   .setType( iInfo.einfo.chooseType.toValue() )
												   .setCount( iInfo.einfo.inavValue )
												   .addAllSelecters( locations )
												   .setIndex(index).build();
	
		ByteString msgByte = HandlerUtil.CREATE_S2G_SIMPLEMESSAGE( SimpleProto.S2G_MSGTYPE.S2G_NOTIFY_CHOOSE_CHARACTER , msg.toByteString()).toByteString();
		room.session().sendMessage( HandlerUtil.CREATE_S2G_GLOBALMESSAGE( GlobalProto.MESSAGE.MESSAGE_SIMPLE_S2G, msgByte) );
						
		// 设置默认数据
		setDefaultChooseInfo(info,iInfo,room);
		
		// 设置延时任务 定时自动触发
		room.getMessageDispatcher().DispatchDelayedTask( new Task_StateRun(room.ID()),room.config().getChooseGapTime());
		return 0;
	}
	
	//
	public void setDefaultChooseInfo(PerformInfo info, InteractiveInfo iInfo, GM_BaseRoom room)
	{
		iInfo.pids.clear();
		
		Vector<Integer> locations = room.getEntityManager()
				.getVaildLocation(iInfo.einfo.chooseType, iInfo.einfo.campType , info.pid);
		
		if(iInfo.einfo.chooseType == CHOOSE_TYPE.PLAYER)
		{
			for(int i=0; i < iInfo.einfo.inavValue; ++i)
			{	
				BasePlayer player = (BasePlayer) room.getEntityManager().getPlayerByLocation( locations.get(i) );
				iInfo.pids.add( new PidValues( player.GetPlayerGuid() ) );
			}
		}else if( iInfo.einfo.chooseType == CHOOSE_TYPE.HANDCARD )
		{
			BasePlayer player = (BasePlayer) room.getEntityManager().getPlayerByLocation( locations.get(0) );
			Vector<Integer> cards = new Vector<Integer>();
				
			for(int j=0; j < Math.min( iInfo.einfo.inavValue, cards.size() )  ; ++j)
			{	
				 int cardid = player.seekHandCards().get(j).getId();
				 cards.add( new Integer(cardid) );
			}
			
			iInfo.pids.add( new PidValues( player.GetPlayerGuid(),cards ) );
		}else if( iInfo.einfo.chooseType == CHOOSE_TYPE.EQUIPMENT )
		{
			BasePlayer player = (BasePlayer) room.getEntityManager().getPlayerByLocation( locations.get(0) );
			Vector<Integer> cards = new Vector<Integer>();
			
			int count = Math.min(2, iInfo.einfo.inavValue);
			if( count-- > 0 )
				cards.add( player.getWeapon().getId() );
			if( count-- > 0 )
				cards.add( player.getArmor().getId() );
			
			iInfo.pids.add( new PidValues( player.GetPlayerGuid(),cards ) );
		}else if( iInfo.einfo.chooseType == CHOOSE_TYPE.ALLCARD )
		{
			BasePlayer player = (BasePlayer) room.getEntityManager().getPlayerByLocation( locations.get(0) );
			Vector<Integer> cards = new Vector<Integer>();
			
			int count = Math.min( iInfo.einfo.inavValue, cards.size() + player.getEquipmentCount() );
			if( count-- > 0 )
				cards.add( player.getWeapon().getId() );
			if( count-- > 0 )
				cards.add( player.getArmor().getId() );
			
			for(int j=0; j < count  ; ++j)
			{					
				if(player.seekHandCards().size() <= j)
					cards.add(  player.seekHandCards().get(j).getId() );
			}
			iInfo.pids.add( new PidValues( player.GetPlayerGuid(),cards ) );
		}
	}	
}














