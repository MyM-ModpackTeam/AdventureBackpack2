package com.darkona.adventurebackpack.client.render;

import com.darkona.adventurebackpack.AdventureBackpack;
import com.darkona.adventurebackpack.client.models.ModelBackpackArmor;
import com.darkona.adventurebackpack.inventory.InventoryItem;
import com.darkona.adventurebackpack.item.ItemAdventureBackpack;
import com.darkona.adventurebackpack.reference.BackpackNames;
import com.darkona.adventurebackpack.reference.ModInfo;
import com.darkona.adventurebackpack.util.LogHelper;
import com.darkona.adventurebackpack.util.Resources;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

/**
 * Created on 25/12/2014
 *
 * @author Darkona
 */
public class RendererBackpackArmor extends RendererLivingEntity
{

    public ResourceLocation texture = new ResourceLocation(ModInfo.MOD_ID,"textures/backpack/Standard.png");
    public ModelBiped modelBipedMain = new ModelBackpackArmor();

    public RendererBackpackArmor()
    {
        super(new ModelBiped(0.0F), 0.5F);
        this.mainModel = this.modelBipedMain = new ModelBackpackArmor();
        this.renderManager=RenderManager.instance;
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity par1Entity)
    {
        return this.texture;
    }

    public void render(Entity entity, double x, double y, double z, float yaw, float pitch, ItemStack backpack, float rotX, float rotY, float rotZ){

        InventoryItem inv = new InventoryItem(backpack);
        //((ModelBackpackArmor)this.modelBipedMain) = backpack.getItem().getArmorModel((EntityLivingBase)entity,backpack, 0);
        ((ModelBackpackArmor)this.modelBipedMain).setBackpack2(backpack);
        this.mainModel = this.modelBipedMain;
        this.modelBipedMain.bipedBody.rotateAngleX = rotX;
        this.modelBipedMain.bipedBody.rotateAngleY = rotY;
        this.modelBipedMain.bipedBody.rotateAngleZ = rotZ;
        ResourceLocation modelTexture;
        if(BackpackNames.getBackpackColorName(backpack).equals("Standard"))
        {
            modelTexture = Resources.backpackTextureFromString(AdventureBackpack.instance.Holiday);
        }
        else
        {
            modelTexture = Resources.backpackTextureFromColor(inv);
        }
        this.texture = modelTexture;
        GL11.glColor3f(1.0F, 1.0F, 1.0F);
        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_CULL_FACE);
        try
        {
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            this.renderMainModel((AbstractClientPlayer)entity, 0, 0, 0, 0, 0, 0.0625f);
        }
        catch(Exception oops)
        {
            //No one cares
        }
        OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glPopMatrix();
    }

    protected void renderMainModel(EntityLivingBase entity, float limbSwing1, float limbswing2, float z, float yaw, float whatever, float scale)
    {
        bindTexture(this.texture);
        if (!entity.isInvisible())
        {
            this.mainModel.render(entity, limbSwing1, limbswing2, z, yaw, whatever, scale);
        }
        else if (!entity.isInvisibleToPlayer(Minecraft.getMinecraft().thePlayer))
        {
            GL11.glPushMatrix();
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.15F);
            GL11.glDepthMask(false);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glAlphaFunc(GL11.GL_GREATER, 0.003921569F);
            this.modelBipedMain.render(entity, limbSwing1, limbswing2, z, yaw, whatever, scale);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
            GL11.glPopMatrix();
            GL11.glDepthMask(true);
        }
        else
        {
            this.mainModel.setRotationAngles(limbSwing1, limbswing2, z, yaw, whatever, scale, entity);
        }
    }
}
