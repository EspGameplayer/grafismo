import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IGraphicElementPos, GraphicElementPos } from '../graphic-element-pos.model';
import { GraphicElementPosService } from '../service/graphic-element-pos.service';

@Component({
  selector: 'jhi-graphic-element-pos-update',
  templateUrl: './graphic-element-pos-update.component.html',
})
export class GraphicElementPosUpdateComponent implements OnInit {
  isSaving = false;

  graphicElementPosSharedCollection: IGraphicElementPos[] = [];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    x: [],
    y: [],
    parent: [],
  });

  constructor(
    protected graphicElementPosService: GraphicElementPosService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ graphicElementPos }) => {
      this.updateForm(graphicElementPos);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const graphicElementPos = this.createFromForm();
    if (graphicElementPos.id !== undefined) {
      this.subscribeToSaveResponse(this.graphicElementPosService.update(graphicElementPos));
    } else {
      this.subscribeToSaveResponse(this.graphicElementPosService.create(graphicElementPos));
    }
  }

  trackGraphicElementPosById(_index: number, item: IGraphicElementPos): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IGraphicElementPos>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(graphicElementPos: IGraphicElementPos): void {
    this.editForm.patchValue({
      id: graphicElementPos.id,
      name: graphicElementPos.name,
      x: graphicElementPos.x,
      y: graphicElementPos.y,
      parent: graphicElementPos.parent,
    });

    this.graphicElementPosSharedCollection = this.graphicElementPosService.addGraphicElementPosToCollectionIfMissing(
      this.graphicElementPosSharedCollection,
      graphicElementPos.parent
    );
  }

  protected loadRelationshipsOptions(): void {
    this.graphicElementPosService
      .query()
      .pipe(map((res: HttpResponse<IGraphicElementPos[]>) => res.body ?? []))
      .pipe(
        map((graphicElementPos: IGraphicElementPos[]) =>
          this.graphicElementPosService.addGraphicElementPosToCollectionIfMissing(graphicElementPos, this.editForm.get('parent')!.value)
        )
      )
      .subscribe((graphicElementPos: IGraphicElementPos[]) => (this.graphicElementPosSharedCollection = graphicElementPos));
  }

  protected createFromForm(): IGraphicElementPos {
    return {
      ...new GraphicElementPos(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      x: this.editForm.get(['x'])!.value,
      y: this.editForm.get(['y'])!.value,
      parent: this.editForm.get(['parent'])!.value,
    };
  }
}
