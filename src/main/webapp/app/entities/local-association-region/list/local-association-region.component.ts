import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ILocalAssociationRegion } from '../local-association-region.model';

import { ASC, DESC, ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { LocalAssociationRegionService } from '../service/local-association-region.service';
import { LocalAssociationRegionDeleteDialogComponent } from '../delete/local-association-region-delete-dialog.component';
import { ParseLinks } from 'app/core/util/parse-links.service';

@Component({
  selector: 'jhi-local-association-region',
  templateUrl: './local-association-region.component.html',
})
export class LocalAssociationRegionComponent implements OnInit {
  localAssociationRegions: ILocalAssociationRegion[];
  isLoading = false;
  itemsPerPage: number;
  links: { [key: string]: number };
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected localAssociationRegionService: LocalAssociationRegionService,
    protected modalService: NgbModal,
    protected parseLinks: ParseLinks
  ) {
    this.localAssociationRegions = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.isLoading = true;

    this.localAssociationRegionService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe({
        next: (res: HttpResponse<ILocalAssociationRegion[]>) => {
          this.isLoading = false;
          this.paginateLocalAssociationRegions(res.body, res.headers);
        },
        error: () => {
          this.isLoading = false;
        },
      });
  }

  reset(): void {
    this.page = 0;
    this.localAssociationRegions = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: ILocalAssociationRegion): number {
    return item.id!;
  }

  delete(localAssociationRegion: ILocalAssociationRegion): void {
    const modalRef = this.modalService.open(LocalAssociationRegionDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.localAssociationRegion = localAssociationRegion;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.reset();
      }
    });
  }

  protected sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? ASC : DESC)];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateLocalAssociationRegions(data: ILocalAssociationRegion[] | null, headers: HttpHeaders): void {
    const linkHeader = headers.get('link');
    if (linkHeader) {
      this.links = this.parseLinks.parse(linkHeader);
    } else {
      this.links = {
        last: 0,
      };
    }
    if (data) {
      for (const d of data) {
        this.localAssociationRegions.push(d);
      }
    }
  }
}
