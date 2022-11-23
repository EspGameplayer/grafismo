import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { GraphicElementPosComponent } from './list/graphic-element-pos.component';
import { GraphicElementPosDetailComponent } from './detail/graphic-element-pos-detail.component';
import { GraphicElementPosUpdateComponent } from './update/graphic-element-pos-update.component';
import { GraphicElementPosDeleteDialogComponent } from './delete/graphic-element-pos-delete-dialog.component';
import { GraphicElementPosRoutingModule } from './route/graphic-element-pos-routing.module';

@NgModule({
  imports: [SharedModule, GraphicElementPosRoutingModule],
  declarations: [
    GraphicElementPosComponent,
    GraphicElementPosDetailComponent,
    GraphicElementPosUpdateComponent,
    GraphicElementPosDeleteDialogComponent,
  ],
  entryComponents: [GraphicElementPosDeleteDialogComponent],
})
export class GraphicElementPosModule {}
