import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { AssociationComponent } from './list/association.component';
import { AssociationDetailComponent } from './detail/association-detail.component';
import { AssociationUpdateComponent } from './update/association-update.component';
import { AssociationDeleteDialogComponent } from './delete/association-delete-dialog.component';
import { AssociationRoutingModule } from './route/association-routing.module';

@NgModule({
  imports: [SharedModule, AssociationRoutingModule],
  declarations: [AssociationComponent, AssociationDetailComponent, AssociationUpdateComponent, AssociationDeleteDialogComponent],
  entryComponents: [AssociationDeleteDialogComponent],
})
export class AssociationModule {}
