package me.xnuminousx.spirits.ability.water;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.projectkorra.projectkorra.BendingPlayer;
import com.projectkorra.projectkorra.GeneralMethods;
import com.projectkorra.projectkorra.ProjectKorra;
import com.projectkorra.projectkorra.ability.AddonAbility;
import com.projectkorra.projectkorra.ability.WaterAbility;
import com.projectkorra.projectkorra.airbending.Suffocate;
import com.projectkorra.projectkorra.util.ParticleEffect;

import me.xnuminousx.spirits.elements.SpiritElement;
import net.md_5.bungee.api.ChatColor;

public class Purify extends WaterAbility implements AddonAbility {
	
	public LivingEntity target;
	private double range;
	public static Set<Integer> heldEntities = new HashSet<Integer>();
	public byte stage = 0;
	public Location travelLoc = null;
	private long duration;
	public double yaw;
	public Random random;
	private long cooldown;
	private boolean hasReached = true;
	private int ticks;
	private int chargeTicks;
	private long time;
	private boolean charged = false;

	public Purify(Player player) {
		super(player);
		// TODO Auto-generated constructor stub
		if (!bPlayer.canBend(this)) {
			return;
		}
		firstloop: for (int i = 20; i < 100; i++) {
			Location loc = GeneralMethods.getTargetedLocation(player, range);
			for (Entity e : GeneralMethods.getEntitiesAroundPoint(loc, 10)) {
				if (e instanceof LivingEntity && e.getEntityId() != player.getEntityId()) {
					target = (LivingEntity) e;
					break firstloop;
				}
			}
		}
		
		duration = 20000;
		cooldown = 5000;
		range = 10;
		time = System.currentTimeMillis();
		
		if (target == null) {
			return;
		}
		heldEntities.add(target.getEntityId());
		start();
	}
	
	public double calculateSize(LivingEntity entity) {
		return (entity.getEyeLocation().distance(entity.getLocation()) / 2 + 0.8D);
	}
	
	@Override
	public void remove() {
		super.remove();
		
		if (target != null) {
			heldEntities.remove(target.getEntityId());
		}
	}

	@Override
	public long getCooldown() {
		// TODO Auto-generated method stub
		return cooldown;
	}

	@Override
	public Location getLocation() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Purify";
	}

	@Override
	public boolean isExplosiveAbility() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isHarmlessAbility() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isIgniteAbility() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isSneakAbility() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void progress() {
		// TODO Auto-generated method stub
		if (!bPlayer.canBendIgnoreCooldowns(this)) {
			remove();
			return;
		}
		
		if (target == null || target.isDead()) {
			remove();
			return;
		}
		
		
		if (!target.getWorld().equals(player.getWorld())) {
			remove();
			return;
		}
		
		if (target.getLocation().distance(player.getLocation()) > 25) {
			
			remove();
			return;
		}
		
		if (System.currentTimeMillis() - time > 10000L) {
			MovementHandler mh = new MovementHandler((Player) player);
			mh.stop(0/1000*20, ChatColor.YELLOW + "" + ChatColor.BOLD + "" + ChatColor.UNDERLINE + "* READY *");
			charged = true;
			createNewSpirals();
		}
		
		if (System.currentTimeMillis() - time > duration) {
			remove();
			bPlayer.addCooldown(this);
			return;
		}
		
		if (charged) {
			if (!player.isSneaking()) {
				if (target instanceof OfflinePlayer) {
					BendingPlayer bPlayer = BendingPlayer.getBendingPlayer((OfflinePlayer) target);
					if (bPlayer.hasElement(SpiritElement.DARK_SPIRIT) && bPlayer.hasElement(SpiritElement.SPIRIT)) {
						bPlayer.addElement(SpiritElement.SPIRIT);
						bPlayer.setElement(SpiritElement.LIGHT_SPIRIT);
						target.sendMessage(SpiritElement.DARK_SPIRIT.getColor() + "You are now a" + ChatColor.BOLD + "" + ChatColor.AQUA + " LightSpirit");
						ParticleEffect.FIREWORKS_SPARK.display(target.getLocation(), (float) Math.random(), (float) Math.random(), (float) Math.random(), 0.0F, 3);
					} else if (bPlayer.hasElement(SpiritElement.DARK_SPIRIT)) {
						bPlayer.setElement(SpiritElement.LIGHT_SPIRIT);
						target.sendMessage(SpiritElement.DARK_SPIRIT.getColor() + "You are now a" + ChatColor.BOLD + "" + ChatColor.AQUA + " LightSpirit");
						ParticleEffect.FIREWORKS_SPARK.display(target.getLocation(), (float) Math.random(), (float) Math.random(), (float) Math.random(), 0.0F, 3);
				} else if (target instanceof Entity || target instanceof LivingEntity) {
					target.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 300, 2));
					target.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 300, 2));
					target.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 300, 2));
					ParticleEffect.FIREWORKS_SPARK.display(target.getLocation(), (float) Math.random(), (float) Math.random(), (float) Math.random(), 0.0F, 3);
				}
			}
		}
		
		if (stage == 0) {
			
			if (!player.isSneaking()) {
				bPlayer.addCooldown(this);
				remove();
				return;
			}
			
			if (travelLoc == null && this.getStartTime() + duration < System.currentTimeMillis()) {
				remove();
				bPlayer.addCooldown(this);
				travelLoc = player.getEyeLocation();
				return;
			} else if (travelLoc == null) {
				ticks++;
				Long chargingTime = System.currentTimeMillis() - getStartTime();
				this.chargeTicks = (int) (chargingTime / 25);
				if (!charged) {
					createSpirals();
				} else {
					createNewSpirals();
				}
				//ParticleEffect.MAGIC_CRIT.display(0.3F, 0.3F, 0.3F, 0.1F, 8, target.getLocation().clone().add(0, 0.8, 0), 90);
				//f7f2f6
				for (int i = -180; i < 180; i += 10) {
					target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 300, 128));
					target.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 300, 128));
				}
				return;
			}
		}
	}
	
	public void paralyze(Entity entity) {
		if (entity instanceof Creature) {
			((Creature) entity).setTarget(null);
		}
		
		if (entity instanceof Player) {
			if (Suffocate.isChannelingSphere((Player) entity)) {
				Suffocate.remove((Player) entity);
			}
		} 
		MovementHandler mh = new MovementHandler((LivingEntity) entity);
		mh.stop(duration/1000*20, ChatColor.YELLOW + "* PURIFYING *");
	}
	
	private void createSpirals() {
		if (hasReached) {
			int amount = chargeTicks + 2;
			double maxHeight = 4;
			double distanceFromPlayer = 1.5;

			int angle = 5 * amount + 5 * ticks;
			double x = Math.cos(Math.toRadians(angle)) * distanceFromPlayer;
			double z = Math.sin(Math.toRadians(angle)) * distanceFromPlayer;
			double height = (amount * 0.10) % maxHeight;
			Location displayLoc = target.getLocation().clone().add(x, height, z);

			int angle2 = 5 * amount + 180 + 5 * ticks;
			double x2 = Math.cos(Math.toRadians(angle2)) * distanceFromPlayer;
			double z2 = Math.sin(Math.toRadians(angle2)) * distanceFromPlayer;
			Location displayLoc2 = target.getLocation().clone().add(x2, height, z2);
			GeneralMethods.displayColoredParticle(displayLoc2, "42aaf4", 0, 0, 0);
			GeneralMethods.displayColoredParticle(displayLoc, "42aaf4", 0, 0, 0);
			GeneralMethods.displayColoredParticle(displayLoc2, "70ddff", 0, 0, 0);
			GeneralMethods.displayColoredParticle(displayLoc, "70ddff", 0, 0, 0);
			GeneralMethods.displayColoredParticle(displayLoc2, ParticleEffect.MOB_SPELL, "42aaf4", 0, 0, 0);
			GeneralMethods.displayColoredParticle(displayLoc, ParticleEffect.MOB_SPELL, "42aaf4", 0, 0, 0);
		}
	}
	
	private void createNewSpirals() {
		if (hasReached) {
			int amount = chargeTicks + 2;
			double maxHeight = 4;
			double distanceFromPlayer = 1.5;

			int angle = 5 * amount + 5 * ticks;
			double x = Math.cos(Math.toRadians(angle)) * distanceFromPlayer;
			double z = Math.sin(Math.toRadians(angle)) * distanceFromPlayer;
			double height = (amount * 0.10) % maxHeight;
			Location displayLoc = target.getLocation().clone().add(x, height, z);

			int angle2 = 5 * amount + 180 + 5 * ticks;
			double x2 = Math.cos(Math.toRadians(angle2)) * distanceFromPlayer;
			double z2 = Math.sin(Math.toRadians(angle2)) * distanceFromPlayer;
			Location displayLoc2 = target.getLocation().clone().add(x2, height, z2);
			GeneralMethods.displayColoredParticle(displayLoc2, "faff9b", 0, 0, 0);
			GeneralMethods.displayColoredParticle(displayLoc, "faff9b", 0, 0, 0);
			GeneralMethods.displayColoredParticle(displayLoc2, "f6ff5e", 0, 0, 0);
			GeneralMethods.displayColoredParticle(displayLoc, "f6ff5e", 0, 0, 0);
		}
	}

	@Override
	public String getAuthor() {
		// TODO Auto-generated method stub
		return "Prride";
	}
	
	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "Cage them nibbas";
	}

	@Override
	public String getVersion() {
		// TODO Auto-generated method stub
		return "Build v1.0";
	}

	@Override
	public void load() {
		// TODO Auto-generated method stub
		ProjectKorra.plugin.getServer().getPluginManager().registerEvents(new PurifyListener(), ProjectKorra.plugin);
		ProjectKorra.log.info(getName() + " " + getVersion() + " by " + getAuthor() + " loaded! ");
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		super.remove();
		ProjectKorra.log.info(getName() + " " + getVersion() + " by " + getAuthor() + " stopped! ");
	}

}
