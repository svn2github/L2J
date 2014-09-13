package npc.model;

import l2next.gameserver.model.Player;
import l2next.gameserver.model.base.AcquireType;
import l2next.gameserver.model.instances.NpcInstance;
import l2next.gameserver.templates.npc.NpcTemplate;
import l2next.gameserver.utils.CertificationFunctions;

public final class DrundumInstance extends NpcInstance
{
	private static final long serialVersionUID = 1L;

	public DrundumInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}

	@Override
	public void onBypassFeedback(Player player, String command)
	{
		if(!canBypassCheck(player, this))
		{
			return;
		}

		if(command.equalsIgnoreCase("CertificationList"))
		{
			CertificationFunctions.showCertificationList(this, player);
		}
		else if(command.equalsIgnoreCase("DualCertificationList"))
		{
			CertificationFunctions.showDualCertificationList(this, player);
		}
		else if(command.equalsIgnoreCase("GetCertification65"))
		{
			CertificationFunctions.getCertification65(this, player);
		}
		else if(command.equalsIgnoreCase("GetCertification70"))
		{
			CertificationFunctions.getCertification70(this, player);
		}
		else if(command.equalsIgnoreCase("GetCertification80"))
		{
			CertificationFunctions.getCertification80(this, player);
		}
		else if(command.equalsIgnoreCase("GetCertification75"))
		{
			CertificationFunctions.getCertification75List(this, player);
		}
		else if(command.equalsIgnoreCase("GetCertification75C"))
		{
			CertificationFunctions.getCertification75(this, player, true);
		}
		else if(command.equalsIgnoreCase("GetCertification75M"))
		{
			CertificationFunctions.getCertification75(this, player, false);
		}
		else if(command.equalsIgnoreCase("CertificationSkillList"))
		{
			showSertifikationSkillList(player, AcquireType.CERTIFICATION);
		}
		else if(command.equalsIgnoreCase("DoubleCertificationSkillList"))
		{
			showDoubleSertifikationSkillList(player, AcquireType.CERTIFICATION_DOUBLE);
		}
		else if(command.equalsIgnoreCase("CertificationCancel"))
		{
			CertificationFunctions.cancelCertification(this, player);
		}
		else if(command.equalsIgnoreCase("DoubleCertificationCancel"))
		{
			CertificationFunctions.cancelDualCertification(this, player);
		}
		else if(command.equalsIgnoreCase("GetCertification85"))
		{
			CertificationFunctions.getCertification85(this, player);
		}
		else if(command.equalsIgnoreCase("GetCertification90"))
		{
			CertificationFunctions.getCertification90(this, player);
		}
		else if(command.equalsIgnoreCase("GetCertification95"))
		{
			CertificationFunctions.getCertification95(this, player);
		}
		else if(command.equalsIgnoreCase("GetCertification99"))
		{
			CertificationFunctions.getCertification99(this, player);
		}
		else
		{
			super.onBypassFeedback(player, command);
		}
	}

	public void showSertifikationSkillList(Player player, AcquireType type)
	{
		showAcquireList(type, player);
	}

	public void showDoubleSertifikationSkillList(Player player, AcquireType type)
	{
		showAcquireList(type, player);
	}

}