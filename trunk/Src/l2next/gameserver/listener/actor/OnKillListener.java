package l2next.gameserver.listener.actor;

import l2next.gameserver.listener.CharListener;
import l2next.gameserver.model.Creature;

public interface OnKillListener extends CharListener
{
	public void onKill(Creature actor, Creature victim);

	/**
	 * FIXME [VISTALL] Когда на игрока добавить OnKillListener, он не добавляется суммону, и нужно вручну добавлять но при ресумоне, проследить трудно
	 * Если возратить тру, то с убийцы будет братся игрок, и на нем вызывать onKill
	 *
	 * @return
	 */
	public boolean ignorePetOrSummon();
}
